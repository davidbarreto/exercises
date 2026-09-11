package br.com.dbarreto.ratelimiter.impl;

import br.com.dbarreto.ratelimiter.RateLimiter;

import java.io.Closeable;
import java.time.Clock;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SlidingWindowRateLimiter implements RateLimiter, Closeable {

    private final int maxRequests;
    private final long windowMillis;
    private final Clock clock;
    private final ScheduledExecutorService cleaner;
    private final Map<String, SlidingWindow> windowMap;


    public SlidingWindowRateLimiter(int maxRequests, long windowMillis) {
        this(Clock.systemDefaultZone(), Executors.newSingleThreadScheduledExecutor(), maxRequests, windowMillis);
    }

    public SlidingWindowRateLimiter(Clock clock, ScheduledExecutorService cleaner, int maxRequests, long windowMillis) {
        this.clock = clock;
        this.cleaner = cleaner;
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
        this.windowMap = new ConcurrentHashMap<>();
        this.cleaner.scheduleAtFixedRate(this::clean, windowMillis, windowMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean allowRequest(String clientId) {
        var window = windowMap.computeIfAbsent(clientId, k -> new SlidingWindow());
        return window.allowRequest(clock.millis(), maxRequests, windowMillis);
    }

    private void clean() {
        for (String clientId : windowMap.keySet()) {
            windowMap.computeIfPresent(clientId, (id, w) -> {
                w.removeStaleRequests(windowMillis, clock.millis());
                return w.isEmpty() ? null : w;
            });
        }
    }

    @Override
    public void close() {
        this.cleaner.shutdown();
    }
}

class SlidingWindow {

    private final Queue<Long> window;

    public SlidingWindow() {
        this.window = new LinkedList<>();
    }

    synchronized boolean allowRequest(long timestamp, int maxRequests, long windowMillis) {

        removeStaleRequests(windowMillis, timestamp);
        if (window.size() < maxRequests) {
            return window.add(timestamp);
        }
        return false;
    }

    synchronized void removeStaleRequests(long windowMillis, long now) {
        while (!window.isEmpty() && now > windowMillis + window.peek()) {
            window.poll();
        }
    }

    synchronized boolean isEmpty() {
        return window.isEmpty();
    }
}
