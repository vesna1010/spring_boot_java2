package com.vesna1010.college.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.vesna1010.college.models.Department;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.repositories.StudyProgramRepository;

public class StudyProgramRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private StudyProgramRepository repository;

	@Test
	public void findAllStudyProgramsWithSortTest() {
		List<StudyProgram> studyPrograms = repository.findAll(SORT);

		assertThat(studyPrograms, hasSize(3));
		assertThat(studyPrograms.get(0).getName(), is("Study Program A"));
		assertThat(studyPrograms.get(1).getName(), is("Study Program B"));
		assertThat(studyPrograms.get(2).getName(), is("Study Program C"));
	}

	@Test
	public void findAllStudyProgramsWithPageableTest() {
		Page<StudyProgram> page = repository.findAll(PAGEABLE);
		List<StudyProgram> studyPrograms = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(studyPrograms, hasSize(3));
		assertThat(studyPrograms.get(0).getName(), is("Study Program B"));
		assertThat(studyPrograms.get(1).getName(), is("Study Program C"));
		assertThat(studyPrograms.get(2).getName(), is("Study Program A"));
	}

	@Test
	public void findAllStudyProgramsByDepartmentIdWithPageableTest() {
		Page<StudyProgram> page = repository.findAllByDepartmentId(1L, PAGEABLE);
		List<StudyProgram> studyPrograms = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(studyPrograms, hasSize(2));
		assertThat(studyPrograms.get(0).getName(), is("Study Program B"));
		assertThat(studyPrograms.get(1).getName(), is("Study Program A"));
	}

	@Test
	public void findStudyProgramByIdTest() {
		Optional<StudyProgram> optional = repository.findById(1L);
		StudyProgram studyProgram = optional.get();

		assertThat(studyProgram.getName(), is("Study Program B"));
		assertThat(studyProgram.getStudents(), hasSize(2));
		assertThat(studyProgram.getSubjects(), hasSize(2));
	}

	@Test
	public void findStudyProgramByIdNotFoundTest() {
		Optional<StudyProgram> optional = repository.findById(4L);

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveStudyProgramTest() {
		StudyProgram studyProgram = new StudyProgram("Study Program", LocalDate.now(), 3,
				new Department(1L, "Department B"));

		studyProgram = repository.save(studyProgram);

		Long id = studyProgram.getId();

		assertTrue(repository.existsById(id));
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void updateStudyProgramTest() {
		Optional<StudyProgram> optional = repository.findById(1L);
		StudyProgram studyProgram = optional.get();

		studyProgram.setName("Study Program");
		
		studyProgram = repository.save(studyProgram);

		assertThat(studyProgram.getName(), is("Study Program"));
		assertThat(studyProgram.getStudents(), hasSize(2));
		assertThat(studyProgram.getSubjects(), hasSize(2));
		assertThat(repository.count(), is(3L));
	}

	@Test
	public void deleteStudyProgramByIdTest() {
		repository.deleteById(1L);

		assertFalse(repository.existsById(1L));
		assertThat(repository.count(), is(2L));
	}

}
