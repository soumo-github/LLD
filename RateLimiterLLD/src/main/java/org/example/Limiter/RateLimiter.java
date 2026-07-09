package org.example.Limiter;

import org.example.Enums.RateLimitType;
import org.example.Model.RateLimiterConfig;

public abstract class RateLimiter {

    protected final RateLimiterConfig config;
    protected final RateLimitType type;

    protected RateLimiter(RateLimiterConfig config, RateLimitType type) {
        this.config = config;
        this.type = type;
    }

    public abstract boolean allowRequest(String userId);
}
