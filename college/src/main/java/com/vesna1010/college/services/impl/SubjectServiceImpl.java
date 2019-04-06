package com.vesna1010.college.services.impl;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.repositories.SubjectRepository;
import com.vesna1010.college.services.SubjectService;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectRepository repository;

	@Override
	public List<Subject> findAllSubjectsByStudyProgramId(Long id, Sort sort) {
		return repository.findAllByStudyProgramId(id, sort);
	}

	@Override
	public Page<Subject> findAllSubjects(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Page<Subject> findAllSubjectsByStudyProgramId(Long id, Pageable pageable) {
		return repository.findAllByStudyProgramId(id, pageable);
	}

	@Override
	public Subject findSubjectById(Long id) {
		Optional<Subject> optional = repository.findById(id);

		return optional.orElseThrow(() -> new RuntimeException("No subject found with id " + id));
	}

	@Override
	public Subject saveSubject(Subject subject) {
		return repository.save(subject);
	}

	@Override
	public void deleteSubjectById(Long id) {
		repository.deleteById(id);
	}

}
