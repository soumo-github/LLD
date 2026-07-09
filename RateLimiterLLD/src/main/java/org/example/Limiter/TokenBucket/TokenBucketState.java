package org.example.Limiter.TokenBucket;

public class TokenBucketState {
    private int availableTokens;
    private long lastRefillTime;
    private long lastAccessTime;
    public TokenBucketState(int capacity, long currentTime) {
        this.availableTokens = capacity;
        this.lastRefillTime = currentTime;
        this.lastAccessTime = currentTime;
    }
    public int getAvailableTokens() {
        return availableTokens;
    }

    public void setAvailableTokens(int availableTokens) {
        this.availableTokens = availableTokens;
    }

    public long getLastRefillTime() {
        return lastRefillTime;
    }

    public void setLastRefillTime(long lastRefillTime) {
        this.lastRefillTime = lastRefillTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
