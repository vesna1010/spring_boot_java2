package com.vesna1010.college.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.repositories.SubjectRepository;
import com.vesna1010.college.services.SubjectService;

public class SubjectServiceTest extends BaseServiceTest {

	@Autowired
	private SubjectService service;
	@MockBean
	private SubjectRepository repository;

	@Test
	public void findAllSubjectsByStudyProgramIdWithSortTest() {
		when(repository.findAllByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(new Subject(1L, "Subject A"), new Subject(2L, "Subject B")));

		List<Subject> subjects = service.findAllSubjectsByStudyProgramId(1L, SORT);

		assertThat(subjects, hasSize(2));
		assertThat(subjects.get(0).getName(), is("Subject A"));
		assertThat(subjects.get(1).getName(), is("Subject B"));
		verify(repository, times(1)).findAllByStudyProgramId(1L, SORT);
	}

	@Test
	public void findAllSubjectsWithPageableTest() {
		when(repository.findAll(PAGEABLE)).thenReturn(
				new PageImpl<Subject>(Arrays.asList(new Subject(1L, "Subject A"), new Subject(2L, "Subject B"))));

		Page<Subject> page = service.findAllSubjects(PAGEABLE);
		List<Subject> subjects = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(subjects, hasSize(2));
		assertThat(subjects.get(0).getName(), is("Subject A"));
		assertThat(subjects.get(1).getName(), is("Subject B"));
		verify(repository, times(1)).findAll(PAGEABLE);
	}

	@Test
	public void findAllSubjectsByStudyProgramIdWithPageableTest() {
		when(repository.findAllByStudyProgramId(1L, PAGEABLE)).thenReturn(
				new PageImpl<Subject>(Arrays.asList(new Subject(1L, "Subject A"), new Subject(2L, "Subject B"))));

		Page<Subject> page = service.findAllSubjectsByStudyProgramId(1L, PAGEABLE);
		List<Subject> subjects = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(subjects, hasSize(2));
		assertThat(subjects.get(0).getName(), is("Subject A"));
		assertThat(subjects.get(1).getName(), is("Subject B"));
		verify(repository, times(1)).findAllByStudyProgramId(1L, PAGEABLE);
	}

	@Test
	public void findSubjectByIdTest() {
		when(repository.findById(1L)).thenReturn(Optional.of(new Subject(1L, "Subject")));

		Subject subject = service.findSubjectById(1L);

		assertThat(subject.getName(), is("Subject"));
		verify(repository, times(1)).findById(1L);
	}

	@Test(expected = RuntimeException.class)
	public void findSubjectByIdNotFoundTest() {
		when(repository.findById(5L)).thenReturn(Optional.empty());

		service.findSubjectById(5L);
	}

	@Test
	public void saveSubjectTest() {
		Subject subject = new Subject(1L, "Subject");

		when(repository.save(subject)).thenReturn(subject);

		Subject subjectSaved = service.saveSubject(subject);

		assertThat(subjectSaved.getId(), is(1L));
		verify(repository, times(1)).save(subject);
	}

	@Test
	public void deleteSubjectByIdTest() {
		doNothing().when(repository).deleteById(1L);

		service.deleteSubjectById(1L);

		verify(repository, times(1)).deleteById(1L);
	}

}
