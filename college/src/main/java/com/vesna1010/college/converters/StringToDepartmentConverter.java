package com.vesna1010.college.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.vesna1010.college.models.Department;
import com.vesna1010.college.services.DepartmentService;

@Component
public class StringToDepartmentConverter implements Converter<String, Department> {

	@Autowired
	private DepartmentService service;

	@Override
	public Department convert(String id) {
		return ((id == null || id.isEmpty()) ? null : service.findDepartmentById(Long.valueOf(id)));
	}

}
