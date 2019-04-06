package com.vesna1010.college.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.vesna1010.college.enums.Gender;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.repositories.ProfessorRepository;

public class ProfessorRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private ProfessorRepository repository;

	@Test
	public void findAllProfessorsWithSortTest() {
		List<Professor> professors = repository.findAll(SORT);

		assertThat(professors, hasSize(3));
		assertThat(professors.get(0).getName(), is("Professor A"));
		assertThat(professors.get(1).getName(), is("Professor B"));
		assertThat(professors.get(2).getName(), is("Professor C"));
	}

	@Test
	public void findAllProfessorsByStudyProgramIdWithSortTest() {
		List<Professor> professors = repository.findAllDistinctBySubjectsStudyProgramId(1L, SORT);

		assertThat(professors, hasSize(2));
		assertThat(professors.get(0).getName(), is("Professor B"));
		assertThat(professors.get(1).getName(), is("Professor C"));
	}

	@Test
	public void findAllProfessorsWithPageableTest() {
		Page<Professor> page1 = repository.findAll(PAGEABLE);
		List<Professor> professors = page1.getContent();

		assertThat(page1.getTotalPages(), is(1));
		assertThat(professors, hasSize(3));
		assertThat(professors.get(0).getName(), is("Professor B"));
		assertThat(professors.get(1).getName(), is("Professor C"));
		assertThat(professors.get(2).getName(), is("Professor A"));
	}

	@Test
	public void findAllProfessorsByStudyProgramIdWithPageableTest() {
		Page<Professor> page1 = repository.findAllDistinctBySubjectsStudyProgramId(1L, PAGEABLE);
		List<Professor> professors = page1.getContent();

		assertThat(page1.getTotalPages(), is(1));
		assertThat(professors, hasSize(2));
		assertThat(professors.get(0).getName(), is("Professor B"));
		assertThat(professors.get(1).getName(), is("Professor C"));
	}

	@Test
	public void findProfessorByIdTest() {
		Optional<Professor> optional = repository.findById(1L);
		Professor professor = optional.get();

		assertThat(professor.getName(), is("Professor B"));
		assertThat(professor.getSubjects(), hasSize(2));
		assertThat(professor.getExams(), hasSize(2));
	}

	@Test
	public void findProfessorByIdNotFoundTest() {
		Optional<Professor> optional = repository.findById(4L);

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveProfessorTest() {
		Professor professor = new Professor("Professor", "Parent", LocalDate.of(1985, Month.JANUARY, 6),
				"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title");

		professor = repository.save(professor);

		Long id = professor.getId();

		assertTrue(repository.existsById(id));
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void updateProfessorTest() {
		professor1.setName("Professor");
		professor1 = repository.save(professor1);

		Optional<Professor> optional = repository.findById(1L);
		Professor professor = optional.get();

		assertThat(professor.getName(), is("Professor"));
		assertThat(professor.getSubjects(), hasSize(2));
		assertThat(professor.getExams(), hasSize(2));
		assertThat(repository.count(), is(3L));
	}

	@Test
	public void deleteProfessorByIdTest() {
		repository.deleteById(1L);

		assertFalse(repository.existsById(1L));
		assertThat(repository.count(), is(2L));
	}

}
