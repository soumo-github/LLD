package org.example.Limiter.SlidingWindow;

import org.example.Background.CleanupScheduler;
import org.example.Enums.RateLimitType;
import org.example.Limiter.RateLimiter;
import org.example.Model.RateLimiterConfig;

import java.util.Deque;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlidingWindowRateLimiter extends RateLimiter {
    private final Map<String, SlidingWindowState> requestLog = new ConcurrentHashMap<>();
    public SlidingWindowRateLimiter(RateLimiterConfig config) {
        super(config, RateLimitType.SLIDING_WINDOW_COUNTER);
        CleanupScheduler.schedule(this::cleanupExpiredEntries, 1, 30, TimeUnit.SECONDS);
    }
    @Override
    public boolean allowRequest(String userId) {
        long now = System.currentTimeMillis();
        SlidingWindowState state = requestLog.computeIfAbsent(userId, id -> new SlidingWindowState(now));
        synchronized (state){
            state.setLastAccessTime(now);
            Deque<Long> log = state.getRequestLog();
            long windowStart = now - (config.getWindowInSeconds() * 1000L);
            while (!log.isEmpty() && log.peekFirst() <= windowStart)
                log.pollFirst();
            if (log.size() < config.getMaxRequests()){
                log.offerLast(now);
                return true;
            }
            return false;
        }
    }
    private void cleanupExpiredEntries(){
        long now = System.currentTimeMillis();
        long expiry = (long) config.getWindowInSeconds() * 60 * 5;
        requestLog.entrySet().removeIf(entry -> (now - entry.getValue().getLastAccessTime()) >= expiry);
    }
}
