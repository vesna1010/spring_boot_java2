package com.vesna1010.college.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.repositories.StudyProgramRepository;
import com.vesna1010.college.services.StudyProgramService;

public class StudyProgramServiceTest extends BaseServiceTest {

	@Autowired
	private StudyProgramService service;
	@MockBean
	private StudyProgramRepository repository;

	@Test
	public void findAllStudyProgramsWithSortTest() {
		when(repository.findAll(SORT)).thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));

		List<StudyProgram> studyPrograms = service.findAllStudyPrograms(SORT);

		assertThat(studyPrograms, hasSize(3));
		assertThat(studyPrograms.get(0).getName(), is("Study Program A"));
		assertThat(studyPrograms.get(1).getName(), is("Study Program B"));
		assertThat(studyPrograms.get(2).getName(), is("Study Program C"));
		verify(repository, times(1)).findAll(SORT);
	}

	@Test
	public void findAllStudyProgramsWithPageableTest() {
		when(repository.findAll(PAGEABLE)).thenReturn(
				new PageImpl<StudyProgram>(Arrays.asList(studyProgram1, studyProgram2, studyProgram3)));

		Page<StudyProgram> page = service.findAllStudyPrograms(PAGEABLE);
		List<StudyProgram> studyPrograms = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(studyPrograms, hasSize(3));
		assertThat(studyPrograms.get(0).getName(), is("Study Program B"));
		assertThat(studyPrograms.get(1).getName(), is("Study Program C"));
		assertThat(studyPrograms.get(2).getName(), is("Study Program A"));
		verify(repository, times(1)).findAll(PAGEABLE);
	}

	@Test
	public void findAllStudyProgramsByDepartmentIdWithPageableTest() {
		when(repository.findAllByDepartmentId(1L, PAGEABLE))
				.thenReturn(new PageImpl<StudyProgram>(Arrays.asList(studyProgram1, studyProgram3)));

		Page<StudyProgram> page = service.findAllStudyProgramsByDepartmentId(1L, PAGEABLE);
		List<StudyProgram> studyPrograms = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(studyPrograms, hasSize(2));
		assertThat(studyPrograms.get(0).getName(), is("Study Program B"));
		assertThat(studyPrograms.get(1).getName(), is("Study Program A"));
		verify(repository, times(1)).findAllByDepartmentId(1L, PAGEABLE);
	}

	@Test
	public void findStudyProgramByIdTest() {
		when(repository.findById(1L)).thenReturn(Optional.of(studyProgram1));

		StudyProgram studyProgram = service.findStudyProgramById(1L);

		assertThat(studyProgram.getName(), is("Study Program B"));
		verify(repository, times(1)).findById(1L);
	}

	@Test(expected = RuntimeException.class)
	public void findStudyProgramByIdNotFoundTest() {
		when(repository.findById(4L)).thenReturn(Optional.empty());

		service.findStudyProgramById(4L);
	}

	@Test
	public void saveStudyProgramTest() {
		StudyProgram studyProgram = new StudyProgram("StudyProgram", LocalDate.now(), 3, department1);

		when(repository.save(studyProgram))
				.thenReturn(new StudyProgram(4L, "StudyProgram", LocalDate.now(), 3, department1));

		StudyProgram studyProgramSaved = service.saveStudyProgram(studyProgram);

		assertThat(studyProgramSaved.getId(), is(4L));
		verify(repository, times(1)).save(studyProgram);
	}

	@Test
	public void deleteStudyProgramByIdTest() {
		doNothing().when(repository).deleteById(1L);

		service.deleteStudyProgramById(1L);

		verify(repository, times(1)).deleteById(1L);
	}

}
