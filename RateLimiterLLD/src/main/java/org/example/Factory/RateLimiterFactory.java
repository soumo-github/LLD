package org.example.Factory;

import org.example.Enums.RateLimitType;
import org.example.Limiter.FixedWindow.FixedWindowRateLimiter;
import org.example.Limiter.RateLimiter;
import org.example.Limiter.SlidingWindow.SlidingWindowRateLimiter;
import org.example.Limiter.TokenBucket.TokenBucketRateLimiter;
import org.example.Model.RateLimiterConfig;

public class RateLimiterFactory {
    public static RateLimiter createRateLimiter(RateLimitType algo, RateLimiterConfig config){
        return switch (algo){
            case TOKEN_BUCKET -> new TokenBucketRateLimiter(config);
            case FIXED_BUCKET -> new FixedWindowRateLimiter(config);
            case SLIDING_WINDOW_COUNTER -> new SlidingWindowRateLimiter(config);
            default -> throw new IllegalArgumentException("Unknown algorithm " + algo);
        };
    }
}
