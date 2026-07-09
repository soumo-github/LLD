package org.example.Background;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class CleanupScheduler {

    private static final ScheduledExecutorService CLEANER =
            Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("RateLimiter-Cleanup-Thread");
                    return thread;
                }
            });

    private CleanupScheduler() {}

    public static void schedule(Runnable task, long initialDelay, long period, TimeUnit timeUnit){
        CLEANER.scheduleAtFixedRate(task, initialDelay, period, timeUnit);
    }

    public static void shutdown(){
        CLEANER.shutdown();
    }

}
