package org.example.Model;

public interface Connection extends AutoCloseable{

    void execute(String sql);

    boolean isValid();

    @Override
    void close()  throws InterruptedException;

}
