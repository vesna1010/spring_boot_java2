package com.vesna1010.college.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vesna1010.college.models.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

	List<Professor> findAllDistinctBySubjectsStudyProgramId(Long id, Sort sort);

	Page<Professor> findAllDistinctBySubjectsStudyProgramId(Long id, Pageable pageable);

}
