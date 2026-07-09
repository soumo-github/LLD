package org.example.Limiter.FixedWindow;

public class FixedWindowState {

    private long windowStart;
    private int requestCount;
    private long lastAccessTime;

    public FixedWindowState(long currentTime) {
        this.windowStart = currentTime;
        this.requestCount = 1;
        this.lastAccessTime = currentTime;
    }

    public long getWindowStart() {
        return windowStart;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public void incrementRequestCount() {
        requestCount++;
    }

    public void reset(long currentTime) {
        windowStart = currentTime;
        requestCount = 1;
    }
}
