package com.vesna1010.college.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.services.SubjectService;

@Component
public class StringToSubjectConverter implements Converter<String, Subject> {

	@Autowired
	private SubjectService service;

	@Override
	public Subject convert(String id) {
		return ((id == null || id.isEmpty()) ? null : service.findSubjectById(Long.valueOf(id)));
	}

}
