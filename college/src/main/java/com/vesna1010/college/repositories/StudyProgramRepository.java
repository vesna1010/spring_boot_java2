package com.vesna1010.college.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vesna1010.college.models.StudyProgram;

public interface StudyProgramRepository extends JpaRepository<StudyProgram, Long> {

	Page<StudyProgram> findAllByDepartmentId(Long id, Pageable pageable);

}
