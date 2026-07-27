package org.example.Model;

import java.time.Duration;
import java.time.Instant;

public class DBConnection implements Connection{

    private final int id;
    private final Instant createdAt = Instant.now();
    private volatile Instant lastBorrowedTime = Instant.now();
    private volatile Instant lastReturnedTime = Instant.now();

    public DBConnection(int id) {
        this.id = id;
    }

    @Override
    public void execute(String sql) {
        System.out.println("Connection " + id + " executing : " + sql);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void close()  throws InterruptedException{
        destroy();
    }

    public void destroy() {
        System.out.println("Physically closing DB connection : " + id);
    }

    public int getId() {
        return id;
    }

    public boolean isIdleTimeoutExceeded(Duration timeout) {

        return Duration.between(lastReturnedTime, Instant.now())
                .compareTo(timeout) >= 0;
    }

    public void markBorrowed() {
        lastBorrowedTime = Instant.now();
    }

    public void markReturned() {
        lastReturnedTime = Instant.now();
    }

    public boolean isExpired(Duration maxLifetime) {

        return Duration.between(createdAt, Instant.now())
                .compareTo(maxLifetime) >= 0;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastBorrowedTime() {
        return lastBorrowedTime;
    }

    public Instant getLastReturnedTime() {
        return lastReturnedTime;
    }
}
