package com.vesna1010.college.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.services.ProfessorService;

@Component
public class StringToProfessorConverter implements Converter<String, Professor> {

	@Autowired
	private ProfessorService service;

	@Override
	public Professor convert(String id) {
		return ((id == null || id.isEmpty()) ? null : service.findProfessorById(Long.valueOf(id)));
	}

}
