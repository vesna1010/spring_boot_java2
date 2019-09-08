package com.vesna1010.college.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.repositories.SubjectRepository;

public class SubjectRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private SubjectRepository repository;

	@Test
	public void findAllSubjectsByStudyProgramIdWithSortTest() {
		List<Subject> subjects = repository.findAllByStudyProgramId(1L, SORT);

		assertThat(subjects, hasSize(2));
		assertThat(subjects.get(0).getName(), is("Subject A"));
		assertThat(subjects.get(1).getName(), is("Subject B"));
	}

	@Test
	public void findAllSubjectsWithPageableTest() {
		Page<Subject> page = repository.findAll(PAGEABLE);
		List<Subject> subjects = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(subjects, hasSize(4));
		assertThat(subjects.get(0).getName(), is("Subject B"));
		assertThat(subjects.get(1).getName(), is("Subject D"));
		assertThat(subjects.get(2).getName(), is("Subject A"));
		assertThat(subjects.get(3).getName(), is("Subject C"));
	}

	@Test
	public void findAllSubjectsByStudyProgramIdWithPageableTest() {
		Page<Subject> page = repository.findAllByStudyProgramId(1L, PAGEABLE);
		List<Subject> subjects = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(subjects, hasSize(2));
		assertThat(subjects.get(0).getName(), is("Subject B"));
		assertThat(subjects.get(1).getName(), is("Subject A"));
	}

	@Test
	public void findSubjectByIdTest() {
		Optional<Subject> optional = repository.findById(1L);
		Subject subject = optional.get();

		assertThat(subject.getName(), is("Subject B"));
		assertThat(subject.getProfessors(), hasSize(2));
		assertThat(subject.getExams(), hasSize(2));
	}

	@Test
	public void findSubjectByIdNotFoundTest() {
		Optional<Subject> optional = repository.findById(5L);

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveSubjectTest() {
		Subject subject = new Subject("Subject", new StudyProgram(1L, "Study Program B"),
				new HashSet<Professor>(Arrays.asList(new Professor(1L, "Professor B"))));

		subject = repository.save(subject);

		Long id = subject.getId();

		assertTrue(repository.existsById(id));
		assertThat(repository.count(), is(5L));
	}

	@Test
	public void updateSubjectTest() {
		Optional<Subject> optional = repository.findById(1L);
		Subject subject = optional.get();
		Professor professor = new Professor(3L, "Professor A");

		subject.setProfessors(new HashSet<Professor>(Arrays.asList(professor)));
		
		subject = repository.save(subject);

		assertThat(subject.getProfessors(), hasSize(1));
		assertTrue(subject.getProfessors().contains(professor));
		assertThat(subject.getExams(), hasSize(2));
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void deleteSubjectByIdTest() {
		repository.deleteById(1L);

		assertFalse(repository.existsById(1L));
		assertThat(repository.count(), is(3L));
	}

}
