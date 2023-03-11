package com.healthassist.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@WritingConverter
public class LocalDateTimeWriteConverter implements Converter<ZonedDateTime, Long> {
    @Override
    public Long convert(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }
}