package org.example.Config;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadPool {

    public static final ExecutorService executor = Executors.newFixedThreadPool(100);

}
