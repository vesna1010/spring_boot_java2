package com.vesna1010.college.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.vesna1010.college.models.Subject;

public interface SubjectService {

	List<Subject> findAllSubjectsByStudyProgramId(Long id, Sort sort);

	Page<Subject> findAllSubjects(Pageable pageable);

	Page<Subject> findAllSubjectsByStudyProgramId(Long id, Pageable pageable);

	Subject findSubjectById(Long id);

	Subject saveSubject(Subject subject);

	void deleteSubjectById(Long id);

}
