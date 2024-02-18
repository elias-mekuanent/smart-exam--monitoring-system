package com.senpare.authservice;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

public abstract class BaseTest<T> {
    protected static final Clock fixedClock = Clock.fixed(Instant.parse("2022-09-30T10:00:00Z"), ZoneOffset.UTC);
    protected static final LocalDateTime now = LocalDateTime.now(fixedClock);
    @Captor
    protected ArgumentCaptor<T> modelArgumentCaptor;
    @Captor
    protected ArgumentCaptor<Long> idArgumentCaptor;
    @Captor
    protected ArgumentCaptor<String> stringArgumentCaptor;
    @Captor
    protected ArgumentCaptor<Integer> intArgumentCaptor;
}
