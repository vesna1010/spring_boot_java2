package com.vesna1010.college.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.vesna1010.college.models.Student;
import com.vesna1010.college.services.StudentService;

@Component
public class StringToStudentConverter implements Converter<String, Student> {

	@Autowired
	private StudentService service;

	@Override
	public Student convert(String id) {
		return ((id == null || id.isEmpty()) ? null : service.findStudentById(Long.valueOf(id)));
	}

}
