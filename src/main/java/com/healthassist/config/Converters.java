package com.healthassist.config;

import com.healthassist.converter.LocalDateTimeReadConverter;
import com.healthassist.converter.LocalDateTimeWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class Converters {
	@Bean
	public MongoCustomConversions mongoCustomConversions() {
		return new MongoCustomConversions(
				Arrays.asList(new LocalDateTimeWriteConverter(), new LocalDateTimeReadConverter()));
	}
}