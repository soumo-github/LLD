package org.example.Model;

import org.example.Enums.RateLimitType;

public class RateLimiterConfig {

    private final RateLimitType rateLimitType;
    private final int maxRequests;
    private final int windowInSeconds;

    public RateLimiterConfig(RateLimitType rateLimitType, int maxRequests, int windowInSeconds) {
        this.rateLimitType = rateLimitType;
        this.maxRequests = maxRequests;
        this.windowInSeconds = windowInSeconds;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public int getWindowInSeconds() {
        return windowInSeconds;
    }

    public RateLimitType getRateLimitType() {
        return rateLimitType;
    }
}
