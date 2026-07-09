package org.example.Limiter.TokenBucket;

import org.example.Background.CleanupScheduler;
import org.example.Enums.RateLimitType;
import org.example.Limiter.RateLimiter;
import org.example.Model.RateLimiterConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class TokenBucketRateLimiter extends RateLimiter {
    private final Map<String, TokenBucketState> states = new ConcurrentHashMap<>();
    public TokenBucketRateLimiter(RateLimiterConfig config) {
        super(config, RateLimitType.TOKEN_BUCKET);
        CleanupScheduler.schedule(this :: cleanupInactiveUsers, 1, 60, TimeUnit.SECONDS);
    }
    @Override
    public boolean allowRequest(String userId) {
        long now = System.currentTimeMillis();
        TokenBucketState state = states.computeIfAbsent(userId, id -> new TokenBucketState(
                config.getMaxRequests(), now));
        synchronized (state){
            state.setLastAccessTime(now);
            refillTokens(state, now);
            if (state.getAvailableTokens() > 0){
                state.setAvailableTokens(state.getAvailableTokens() - 1);
                return true;
            }
            return false;
        }
    }
    private void refillTokens(TokenBucketState state, long now){

        if(config.getWindowInSeconds() >= config.getMaxRequests()) {

            int refillRate = config.getWindowInSeconds() / config.getMaxRequests();
            long elapsedSeconds = (now - state.getLastRefillTime()) / 1000;
            int refill = (int) elapsedSeconds / (int) refillRate;

            if (refill <= 0)
                return;

            state.setAvailableTokens(Math.min(config.getMaxRequests(), state.getAvailableTokens() + refill));
        }
        else {

            int refillRatePerSecond = config.getMaxRequests() / config.getWindowInSeconds();
            long elapsedMillis = now - state.getLastRefillTime();

            if (elapsedMillis <= 0) {
                return;
            }

            int tokensToAdd = (int) (elapsedMillis * 1000) * refillRatePerSecond;
            if (tokensToAdd >= 1){
                state.setAvailableTokens(Math.min(config.getMaxRequests(), state.getAvailableTokens() + tokensToAdd));
            }
        }
        state.setLastRefillTime(now);
    }

    private void cleanupInactiveUsers() {
        long now = System.currentTimeMillis();
        long expiryTime = (long) config.getWindowInSeconds() * 60 * 5;

        states.entrySet().removeIf(entry -> (now - entry.getValue().getLastAccessTime()) > expiryTime);

    }
}
