package com.vesna1010.college.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.services.StudyProgramService;

@Component
public class StringToStudyProgrammeConverter implements Converter<String, StudyProgram> {

	@Autowired
	private StudyProgramService service;

	@Override
	public StudyProgram convert(String id) {
		return ((id == null || id.isEmpty()) ? null : service.findStudyProgramById(Long.valueOf(id)));
	}

}
