package org.example.Limiter.SlidingWindow;

import java.util.ArrayDeque;
import java.util.Deque;

public class SlidingWindowState {

    private final Deque<Long> requestLog;
    private long lastAccessTime;

    public SlidingWindowState(long lastAccessTime) {
        this.requestLog = new ArrayDeque<>();
        this.lastAccessTime = lastAccessTime;
    }

    public Deque<Long> getRequestLog() {
        return requestLog;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long currentTime) {
        lastAccessTime = currentTime;
    }
}
