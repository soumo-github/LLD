package org.example.Proxy;

import org.example.Config.ConnectionPool;
import org.example.Model.Connection;
import org.example.Model.DBConnection;

public class ProxyConnection implements Connection {

    private final DBConnection connection;
    private final ConnectionPool pool;

    public ProxyConnection(DBConnection connection, ConnectionPool pool) {
        this.connection = connection;
        this.pool = pool;
    }

    @Override
    public void execute(String sql) {
        System.out.println("EXECUTED SQL QUERY");
    }

    @Override
    public boolean isValid() {
        return connection.isValid();  // why not suer.inValid().??
    }

    @Override
    public void close()  throws InterruptedException{
        Thread.sleep(500);
        pool.releaseConnection(connection);
    }
}
