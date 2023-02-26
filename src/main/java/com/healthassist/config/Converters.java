package com.healthassist.config;

import com.healthassist.converter.ZonedDateTimeReadConverter;
import com.healthassist.converter.ZonedDateTimeWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class Converters {
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(
                Arrays.asList(
                        new ZonedDateTimeWriteConverter(),
                        new ZonedDateTimeReadConverter()
                ));
    }
}