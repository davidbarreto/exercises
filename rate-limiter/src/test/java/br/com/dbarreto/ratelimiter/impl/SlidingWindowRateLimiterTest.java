package br.com.dbarreto.ratelimiter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlidingWindowRateLimiterTest {

    @Mock
    Clock clock;

    @Mock
    ScheduledExecutorService cleaner;

    @BeforeEach
    void init() {
        when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:00-03:00").toEpochMilli());
    }

    @Test
    void whenItsFirstRequestShouldAllowIt() {

        //Limiter at 3 requests per 10 seconds
        try (var rateLimiter = new SlidingWindowRateLimiter(clock, cleaner, 3, 10000)) {
            assertTrue(rateLimiter.allowRequest("Client1"));
        }
    }

    @Test
    void whenWindowIsFullShouldBlockNewRequests() {

        //Limiter at 3 requests per 10 seconds
        try (var rateLimiter = new SlidingWindowRateLimiter(clock, cleaner, 3, 10000)) {
            rateLimiter.allowRequest("Client1");
            when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:03-03:00").toEpochMilli());
            rateLimiter.allowRequest("Client1");
            when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:05-03:00").toEpochMilli());
            rateLimiter.allowRequest("Client1");

            // Now we already have maxRequests reached in within windowMillis, so next requests will be blocked
            when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:07-03:00").toEpochMilli());
            assertFalse(rateLimiter.allowRequest("Client1"));
            when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:08-03:00").toEpochMilli());
            assertFalse(rateLimiter.allowRequest("Client1"));
            when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:09-03:00").toEpochMilli());
            assertFalse(rateLimiter.allowRequest("Client1"));
        }
    }

    @Test
    void whenWindowSizePassedShouldAllowNewRequest() {

        //Limiter at 3 requests per 10 seconds
        try (var rateLimiter = new SlidingWindowRateLimiter(clock, cleaner, 3, 10000)) {
            rateLimiter.allowRequest("Client1");
            when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:03-03:00").toEpochMilli());
            rateLimiter.allowRequest("Client1");
            when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:05-03:00").toEpochMilli());
            rateLimiter.allowRequest("Client1");

            // This request will be accepted because first request is already stale
            // (now it's more then windoMillis ms after instant of frist request added)
            when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:11-03:00").toEpochMilli());
            assertTrue(rateLimiter.allowRequest("Client1"));
        }
    }

    @Test
    void whenExactlyAtWindowBoundaryShouldStillBlock() {
        try (var rateLimiter = new SlidingWindowRateLimiter(clock, cleaner, 1, 10000)) {
            rateLimiter.allowRequest("Client1");
            when(clock.millis()).thenReturn(Instant.parse("2020-11-30T19:55:10-03:00").toEpochMilli());
            assertFalse(rateLimiter.allowRequest("Client1")); // exactly windowMillis later, not yet stale
        }
    }

    @Test
    void differentClientsShouldHaveIndependentQuotas() {
        try (var rateLimiter = new SlidingWindowRateLimiter(clock, cleaner, 1, 10000)) {
            assertTrue(rateLimiter.allowRequest("Client1"));
            assertFalse(rateLimiter.allowRequest("Client1"));
            assertTrue(rateLimiter.allowRequest("Client2")); // separate quota
        }
    }
}