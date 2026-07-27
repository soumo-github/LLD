package org.example.Config;
import org.example.Factory.ConnectionFactory;
import org.example.Model.Connection;
import org.example.Model.DBConnection;
import org.example.Proxy.ProxyConnection;

import javax.naming.TimeLimitExceededException;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPool implements AutoCloseable{

    private final BlockingQueue<DBConnection> availableConnections;
    private final Set<DBConnection> borrowedConnections = ConcurrentHashMap.newKeySet();
    private final ConnectionFactory factory;
    private final DatabaseConfig config;
    private final AtomicInteger currentPoolSize = new AtomicInteger();
    private final ScheduledExecutorService houseKeeper = new ScheduledThreadPoolExecutor(1, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("connection-pool-housekeeper");

        return t;
    });

    public ConnectionPool(DatabaseConfig config) {
        this.config = config;
        this.factory = new ConnectionFactory();
        availableConnections = new LinkedBlockingDeque<>();

        for (int i = 0; i < config.getMinimumIdle(); i++)
            availableConnections.offer(factory.createConnection(config));

        currentPoolSize.set(config.getMinimumIdle());
        houseKeeper.scheduleAtFixedRate(this::healthCheck, 5, 5, TimeUnit.MINUTES);
    }
    private void healthCheck(){
        for (DBConnection connection : availableConnections){
            if (!connection.isValid() || connection.isExpired(config.getMaxLifetime())){
                if (availableConnections.remove(connection)) {
                    connection.destroy();
                    availableConnections.offer(factory.createConnection(config));
                }
            }
        }
        shrinkPool();
        detectLeaks();
    }

    private void shrinkPool(){

        while (currentPoolSize.get() > config.getMinimumIdle()){
            DBConnection connection = availableConnections.poll();
            if (connection == null) {
                return;
            }
            if (!connection.isIdleTimeoutExceeded(config.getIdleTimeout())) {
                availableConnections.offer(connection);
                return;
            }
            connection.destroy();
            currentPoolSize.decrementAndGet();
        }
    }

    private void detectLeaks() {
        for (DBConnection connection : borrowedConnections) {
            if (Duration.between(connection.getLastBorrowedTime(), Instant.now()).compareTo(config.getLeakDetectionThreshold()) > 0) {
                System.out.println(
                        "Connection Leak Detected : " + connection.getId()
                );
            }
        }
    }


    public Connection acquireConnection() throws TimeLimitExceededException, InterruptedException {
        if (availableConnections.isEmpty()){
            while (true) {

                int current = currentPoolSize.get();
                if (current >= config.getMaxPoolSize())
                    break;

                if (currentPoolSize.compareAndSet(current, current + 1)){
                    availableConnections.offer(factory.createConnection(config));
                    break;
                }
            }
        }
        DBConnection connection = availableConnections.poll(config.getTime(), config.getTimeUnit());
        if (connection == null) {
            throw new TimeLimitExceededException(
                    "Could not acquire connection within: " + config.getTime() + " seconds.");
        }
        borrowedConnections.add(connection);
        return new ProxyConnection(connection, this);
    }

    public void releaseConnection(DBConnection connection){
        if (connection == null)
            return;

        if (!borrowedConnections.remove(connection))
            throw new IllegalArgumentException("Connection not from this pool");

        System.out.println("Closing connection: " + connection.getId());
        availableConnections.offer(connection);
    }

    @Override
    public void close() throws Exception {
        for (DBConnection connection : availableConnections)
            connection.close();

        for (DBConnection connection : borrowedConnections)
            connection.close();

        availableConnections.clear();
        borrowedConnections.clear();
        houseKeeper.shutdown();
    }
}
