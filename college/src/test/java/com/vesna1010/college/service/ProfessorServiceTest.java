package com.vesna1010.college.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.vesna1010.college.enums.Gender;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.repositories.ProfessorRepository;
import com.vesna1010.college.services.ProfessorService;

public class ProfessorServiceTest extends BaseServiceTest {

	@Autowired
	private ProfessorService service;
	@MockBean
	private ProfessorRepository repository;

	@Test
	public void findAllProfessorsWithSortTest() {
		when(repository.findAll(SORT)).thenReturn(Arrays.asList(professor3, professor1, professor2));

		List<Professor> professors = service.findAllProfessors(SORT);

		assertThat(professors, hasSize(3));
		assertThat(professors.get(0).getName(), is("Professor A"));
		assertThat(professors.get(1).getName(), is("Professor B"));
		assertThat(professors.get(2).getName(), is("Professor C"));
		verify(repository, times(1)).findAll(SORT);
	}

	@Test
	public void findAllProfessorsByStudyProgramWithSortTest() {
		when(repository.findAllDistinctBySubjectsStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));

		List<Professor> professors = service.findAllProfessorsByStudyProgramId(1L, SORT);

		assertThat(professors, hasSize(2));
		assertThat(professors.get(0).getName(), is("Professor B"));
		assertThat(professors.get(1).getName(), is("Professor C"));
		verify(repository, times(1)).findAllDistinctBySubjectsStudyProgramId(1L, SORT);
	}

	@Test
	public void findAllProfessorsWithPageableTest() {
		when(repository.findAll(PAGEABLE))
				.thenReturn(new PageImpl<Professor>(Arrays.asList(professor1, professor2, professor3)));

		Page<Professor> page = service.findAllProfessors(PAGEABLE);
		List<Professor> professors = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(professors, hasSize(3));
		assertThat(professors.get(0).getName(), is("Professor B"));
		assertThat(professors.get(1).getName(), is("Professor C"));
		assertThat(professors.get(2).getName(), is("Professor A"));
		verify(repository, times(1)).findAll(PAGEABLE);
	}

	@Test
	public void findAllProfessorsByStudyProgramWithPageableTest() {
		when(repository.findAllDistinctBySubjectsStudyProgramId(1L, PAGEABLE))
				.thenReturn(new PageImpl<Professor>(Arrays.asList(professor1, professor2)));

		Page<Professor> page = service.findAllProfessorsByStudyProgramId(1L, PAGEABLE);
		List<Professor> professors = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(professors, hasSize(2));
		assertThat(professors.get(0).getName(), is("Professor B"));
		assertThat(professors.get(1).getName(), is("Professor C"));
		verify(repository, times(1)).findAllDistinctBySubjectsStudyProgramId(1L, PAGEABLE);
	}

	@Test
	public void findProfessorByIdTest() {
		when(repository.findById(1L)).thenReturn(Optional.of(professor1));

		Professor professor = service.findProfessorById(1L);

		assertThat(professor.getName(), is("Professor B"));
		verify(repository, times(1)).findById(1L);
	}

	@Test(expected = RuntimeException.class)
	public void findProfessorByIdNotFoundTest() {
		when(repository.findById(4L)).thenReturn(Optional.empty());

		service.findProfessorById(4L);
	}

	@Test
	public void saveStudentTest() {
		Professor professor = new Professor("Professor", "Parent", LocalDate.of(1985, Month.JANUARY, 6),
				"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title");

		when(repository.save(professor))
				.thenReturn(new Professor(4L, "Professor", "Parent", LocalDate.of(1995, Month.JANUARY, 6),
						"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title"));

		Professor professorSaved = service.saveProfessor(professor);

		assertThat(professorSaved.getId(), is(4L));
		verify(repository, times(1)).save(professor);
	}

	@Test
	public void deleteProfessorByIdTest() {
		doNothing().when(repository).deleteById(1L);

		service.deleteProfessorById(1L);

		verify(repository, times(1)).deleteById(1L);
	}

}
