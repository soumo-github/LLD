package org.example.Config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DatabaseConfig {

    private final int minimumIdle;
    private final String url;
    private final String username;
    private final String password;
    private final int maxPoolSize;
    private final int time;
    private final TimeUnit timeUnit;
    private final Duration idleTimeout;
    private final Duration maxLifetime;
    private final Duration leakDetectionThreshold;

    public DatabaseConfig(String url, String username, String password, int minimumIdle, int maxPoolSize, int time, TimeUnit timeUnit, Duration idleTimeout, Duration maxLifetime, Duration leakDetectionThreshold) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.minimumIdle = minimumIdle;
        this.maxPoolSize = maxPoolSize;
        this.time = time;
        this.timeUnit = timeUnit;
        this.idleTimeout = idleTimeout;
        this.maxLifetime = maxLifetime;
        this.leakDetectionThreshold = leakDetectionThreshold;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public int getTime() {
        return time;
    }

    public int getMinimumIdle() {
        return minimumIdle;
    }
    public Duration getIdleTimeout() {
        return idleTimeout;
    }

    public Duration getMaxLifetime() {
        return maxLifetime;
    }

    public Duration getLeakDetectionThreshold() {
        return leakDetectionThreshold;
    }
}
