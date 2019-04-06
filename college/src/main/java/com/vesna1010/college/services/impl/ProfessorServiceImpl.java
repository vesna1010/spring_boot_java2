package com.vesna1010.college.services.impl;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.repositories.ProfessorRepository;
import com.vesna1010.college.services.ProfessorService;

@Service
@Transactional
public class ProfessorServiceImpl implements ProfessorService {

	@Autowired
	private ProfessorRepository repository;

	@Override
	public List<Professor> findAllProfessors(Sort sort) {
		return repository.findAll(sort);
	}

	@Override
	public List<Professor> findAllProfessorsByStudyProgramId(Long id, Sort sort) {
		return repository.findAllDistinctBySubjectsStudyProgramId(id, sort);
	}

	@Override
	public Page<Professor> findAllProfessors(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Page<Professor> findAllProfessorsByStudyProgramId(Long id, Pageable pageable) {
		return repository.findAllDistinctBySubjectsStudyProgramId(id, pageable);
	}

	@Override
	public Professor findProfessorById(Long id) {
		Optional<Professor> optional = repository.findById(id);

		return optional.orElseThrow(() -> new RuntimeException("No professor found with id " + id));
	}

	@Override
	public Professor saveProfessor(Professor professor) {
		return repository.save(professor);
	}

	@Override
	public void deleteProfessorById(Long id) {
		repository.deleteById(id);
	}

}
