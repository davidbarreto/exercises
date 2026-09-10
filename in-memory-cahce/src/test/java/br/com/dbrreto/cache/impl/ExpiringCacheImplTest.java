package br.com.dbrreto.cache.impl;

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
class ExpiringCacheImplTest {

    @Mock
    Clock clock;

    @Mock
    ScheduledExecutorService cleaner;

    @Test
    void whenGetExpiredEntry() {

        when(clock.instant()).thenReturn(Instant.parse("2020-11-30T19:55:00-03:00"));
        try (ExpiringCacheImpl<String, String> cache = new ExpiringCacheImpl<>(clock, cleaner)) {
            cache.put("banana", "yellow", 100);
            when(clock.instant()).thenReturn(Instant.parse("2020-11-30T19:55:01-03:00"));
            var value = cache.get("banana");
            assertNull(value);
        }
    }

    @Test
    void whenGetNonExpiredEntry() {

        when(clock.instant()).thenReturn(Instant.parse("2020-11-30T19:55:00-03:00"));
        try (ExpiringCacheImpl<String, String> cache = new ExpiringCacheImpl<>(clock, cleaner)) {
            cache.put("banana", "yellow", 2000);
            when(clock.instant()).thenReturn(Instant.parse("2020-11-30T19:55:01-03:00"));
            var value = cache.get("banana");
            assertNotNull(value);
            assertEquals("yellow", value);
        }
    }
}