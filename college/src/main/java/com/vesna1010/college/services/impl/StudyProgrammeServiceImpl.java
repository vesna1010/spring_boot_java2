package com.vesna1010.college.services.impl;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.repositories.StudyProgramRepository;
import com.vesna1010.college.services.StudyProgramService;

@Service
@Transactional
public class StudyProgrammeServiceImpl implements StudyProgramService {

	@Autowired
	private StudyProgramRepository repository;

	@Override
	public List<StudyProgram> findAllStudyPrograms(Sort sort) {
		return repository.findAll(sort);
	}

	@Override
	public Page<StudyProgram> findAllStudyPrograms(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Page<StudyProgram> findAllStudyProgramsByDepartmentId(Long id, Pageable pageable) {
		return repository.findAllByDepartmentId(id, pageable);
	}

	@Override
	public StudyProgram findStudyProgramById(Long id) {
		Optional<StudyProgram> optional = repository.findById(id);

		return optional.orElseThrow(() -> new RuntimeException("No study programme found with id " + id));
	}

	@Override
	public StudyProgram saveStudyProgram(StudyProgram studyProgramme) {
		return repository.save(studyProgramme);
	}

	@Override
	public void deleteStudyProgramById(Long id) {
		repository.deleteById(id);
	}

}
