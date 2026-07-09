package org.example.Service;

import org.example.Enums.RateLimitType;
import org.example.Enums.UserTier;
import org.example.Factory.RateLimiterFactory;
import org.example.Limiter.RateLimiter;
import org.example.Model.RateLimiterConfig;
import org.example.Model.User;

import java.util.HashMap;
import java.util.Map;

public class RateLimiterService {

    private final Map<UserTier, RateLimiter> rateLimiters = new HashMap<>();

    public RateLimiterService(Map<UserTier, RateLimiterConfig> configs) {

        for (Map.Entry<UserTier, RateLimiterConfig> entry : configs.entrySet()) {

            UserTier tier = entry.getKey();

            RateLimiterConfig config = entry.getValue();

            rateLimiters.put(
                    tier,
                    RateLimiterFactory.createRateLimiter(
                            config.getRateLimitType(),
                            config
                    )
            );
        }
    }

    public boolean allowRequest(User user){
        RateLimiter limiter = rateLimiters.get(user.getTier());

        if (limiter == null)
            throw new IllegalArgumentException("No Rate Limiter configured for this user tier " + user.getTier());

        return limiter.allowRequest(user.getUserId());
    }
}
//int maxRequests, int windowInSeconds
