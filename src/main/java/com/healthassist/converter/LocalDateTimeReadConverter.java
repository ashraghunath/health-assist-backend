package com.healthassist.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Component
@ReadingConverter
public class LocalDateTimeReadConverter implements Converter<Long, LocalDate> {
    @Override
    public LocalDate convert(Long date) {
        return Instant.ofEpochMilli(date).atZone(ZoneOffset.UTC).toLocalDate();
    }
}