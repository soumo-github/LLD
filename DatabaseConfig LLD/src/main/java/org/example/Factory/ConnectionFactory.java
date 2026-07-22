package org.example.Factory;

import org.example.Config.DatabaseConfig;
import org.example.Model.DBConnection;

import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionFactory {

    private final AtomicInteger counter = new AtomicInteger(0);

    public DBConnection createConnection(DatabaseConfig config){
        return new DBConnection(counter.incrementAndGet());
    }
}
