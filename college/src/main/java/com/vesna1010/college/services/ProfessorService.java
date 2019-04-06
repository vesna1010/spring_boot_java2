package com.vesna1010.college.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.vesna1010.college.models.Professor;

public interface ProfessorService {

	List<Professor> findAllProfessors(Sort sort);

	List<Professor> findAllProfessorsByStudyProgramId(Long id, Sort sort);

	Page<Professor> findAllProfessors(Pageable pageable);

	Page<Professor> findAllProfessorsByStudyProgramId(Long id, Pageable pageable);

	Professor findProfessorById(Long id);

	Professor saveProfessor(Professor professor);

	void deleteProfessorById(Long id);

}
