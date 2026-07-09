package org.example.Limiter.FixedWindow;

import org.example.Background.CleanupScheduler;
import org.example.Enums.RateLimitType;
import org.example.Limiter.RateLimiter;
import org.example.Model.RateLimiterConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class FixedWindowRateLimiter extends RateLimiter {
    private final Map<String, FixedWindowState> states = new ConcurrentHashMap<>();
    public FixedWindowRateLimiter(RateLimiterConfig config) {
        super(config, RateLimitType.FIXED_BUCKET);
        CleanupScheduler.schedule(this::cleanupExpiredUsers, 1, 60, TimeUnit.SECONDS);
    }
    @Override
    public boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        FixedWindowState state = states.computeIfAbsent(userId, id -> new FixedWindowState(currentTime));
        synchronized (state) {
            state.setLastAccessTime(currentTime);
            if (currentTime - state.getWindowStart()
                    >= config.getWindowInSeconds() * 1000L) {
                state.reset(currentTime);
                return true;
            }
            if (state.getRequestCount() < config.getMaxRequests()) {
                state.incrementRequestCount();
                return true;
            }
            return false;
        }
    }
    private void cleanupExpiredUsers(){
        long now = System.currentTimeMillis();
        long expiryTime = (long) config.getWindowInSeconds() * 60 * 5;
        states.entrySet().removeIf(entry ->
            (now - entry.getValue().getLastAccessTime() > expiryTime)
        );
    }
}
