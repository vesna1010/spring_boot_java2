package com.vesna1010.college.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.vesna1010.college.models.StudyProgram;

public interface StudyProgramService {

	List<StudyProgram> findAllStudyPrograms(Sort sort);

	Page<StudyProgram> findAllStudyPrograms(Pageable pageable);

	Page<StudyProgram> findAllStudyProgramsByDepartmentId(Long id, Pageable pageable);

	StudyProgram findStudyProgramById(Long id);

	StudyProgram saveStudyProgram(StudyProgram studyProgram);

	void deleteStudyProgramById(Long id);

}
