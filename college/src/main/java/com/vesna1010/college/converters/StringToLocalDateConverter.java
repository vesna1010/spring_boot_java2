package com.vesna1010.college.converters;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	@Override
	public LocalDate convert(String text) {
		try {
			return LocalDate.parse(text, formatter);
		} catch (DateTimeException e) {
			return null;
		}
	}

}
