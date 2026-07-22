package org.example;

import org.example.Config.ConnectionPool;
import org.example.Config.DatabaseConfig;
import org.example.Model.Connection;

import javax.naming.TimeLimitExceededException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        DatabaseConfig config =
                new DatabaseConfig(
                        "jdbc:mysql://localhost:3306/testdb",
                        "root",
                        "password",
                        2,
                        3,
                        3,
                        TimeUnit.SECONDS,
                        Duration.ofSeconds(5),    // idle timeout
                        Duration.ofMinutes(5),    // max lifetime
                        Duration.ofSeconds(5)      // leak detection
                );

        try (ConnectionPool pool = new ConnectionPool(config)) {

            System.out.println("\n========== CONNECTION POOL STARTED ==========\n");

            System.out.println("Borrowing Connection 1");
            Connection c1 = pool.acquireConnection();
            c1.execute("SELECT * FROM EMPLOYEE");

            System.out.println("Borrowing Connection 2");
            Connection c2 = pool.acquireConnection();
            c2.execute("SELECT * FROM DEPARTMENT");

            System.out.println("Borrowing Connection 3 (Pool should grow)");
            Connection c3 = pool.acquireConnection();
            c3.execute("UPDATE EMPLOYEE SET SALARY = 10000");

            System.out.println("Borrowing Connection 4");
            Connection c4 = pool.acquireConnection();
            c4.execute("DELETE FROM EMPLOYEE WHERE ID = 10");

            System.out.println("Borrowing Connection 5");
            Connection c5 = pool.acquireConnection();
            c5.execute("INSERT INTO EMPLOYEE VALUES (1,'JOHN')");

            System.out.println("\nPool has reached Maximum Pool Size.\n");

            try {
                System.out.println("Trying to acquire one more connection...");
                Connection c6 = pool.acquireConnection();
                c6.execute("SHOULD NOT EXECUTE");
            } catch (TimeLimitExceededException e) {
                System.out.println("Acquire Timeout : " + e.getMessage());
            }

            System.out.println("\nReturning Connection 1");
            c1.close();

            System.out.println("Returning Connection 2");
            c2.close();

            System.out.println("Borrowing Again (Should reuse returned connection)");
            Connection c7 = pool.acquireConnection();
            c7.execute("SELECT NOW()");
            c7.close();

            System.out.println("\nKeeping Connection 3 open...");
            System.out.println("Leak Detector should report this connection.");
            Thread.sleep(7000);

            System.out.println("Returning Connection 3");
            c3.close();

            System.out.println("Returning Connection 4");
            c4.close();

            System.out.println("Returning Connection 5");
            c5.close();

            System.out.println("\nWaiting for HouseKeeper...");
            System.out.println("Idle connections should shrink to Minimum Idle.");
            Thread.sleep(15000);

            System.out.println("\nBorrowing Again After Shrink");
            Connection c8 = pool.acquireConnection();
            c8.execute("SELECT * FROM PRODUCTS");
            c8.close();

            System.out.println("\n========== DEMO COMPLETED ==========\n");
        }

        System.out.println("Connection Pool Closed.");
    }
}