package com.khanhduy14.todolist.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateTimeUtils {
    private static final ZoneId DEFAULT_ZONE = ZoneOffset.UTC;

    private DateTimeUtils() {}

    public static LocalDateTime now() {
        return LocalDateTime.now(DEFAULT_ZONE);
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.toInstant(ZoneOffset.UTC);
    }

    public static LocalDateTime fromInstant(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, DEFAULT_ZONE);
    }
}