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
import com.vesna1010.college.models.Student;
import com.vesna1010.college.repositories.StudentRepository;
import com.vesna1010.college.services.StudentService;

public class StudentServiceTest extends BaseServiceTest {

	@Autowired
	private StudentService service;
	@MockBean
	private StudentRepository repository;

	@Test
	public void findAllStudentsByStudyProgramIdWithSortTest() {
		when(repository.findAllByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(new Student(1L, "Student A"), new Student(2L, "Student B")));

		List<Student> students = service.findAllStudentsByStudyProgramId(1L, SORT);

		assertThat(students, hasSize(2));
		assertThat(students.get(0).getName(), is("Student A"));
		assertThat(students.get(1).getName(), is("Student B"));
		verify(repository, times(1)).findAllByStudyProgramId(1L, SORT);
	}

	@Test
	public void findAllStudentsWithPageableTest() {
		when(repository.findAll(PAGEABLE)).thenReturn(
				new PageImpl<Student>(Arrays.asList(new Student(1L, "Student A"), new Student(2L, "Student B"))));

		Page<Student> page = service.findAllStudents(PAGEABLE);
		List<Student> students = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(students, hasSize(2));
		assertThat(students.get(0).getName(), is("Student A"));
		assertThat(students.get(1).getName(), is("Student B"));
		verify(repository, times(1)).findAll(PAGEABLE);
	}

	@Test
	public void findAllStudentsByStudyProgramIdWithPageableTest() {
		when(repository.findAllByStudyProgramId(1L, PAGEABLE)).thenReturn(
				new PageImpl<Student>(Arrays.asList(new Student(1L, "Student A"), new Student(2L, "Student B"))));

		Page<Student> page = service.findAllStudentsByStudyProgramId(1L, PAGEABLE);
		List<Student> students = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(students, hasSize(2));
		assertThat(students.get(0).getName(), is("Student A"));
		assertThat(students.get(1).getName(), is("Student B"));
		verify(repository, times(1)).findAllByStudyProgramId(1L, PAGEABLE);
	}

	@Test
	public void findStudentByIdTest() {
		when(repository.findById(1L)).thenReturn(Optional.of(new Student(1L, "Student")));

		Student student = service.findStudentById(1L);

		assertThat(student.getName(), is("Student"));
		verify(repository, times(1)).findById(1L);
	}

	@Test(expected = RuntimeException.class)
	public void findStudentByIdNotFoundTest() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		service.findStudentById(1L);
	}

	@Test
	public void saveStudentTest() {
		Student student = new Student(1L, "Student");

		when(repository.save(student)).thenReturn(student);

		Student studentSaved = service.saveStudent(student);

		assertThat(studentSaved.getId(), is(1L));
		verify(repository, times(1)).save(student);
	}

	@Test
	public void deleteStudentByIdTest() {
		doNothing().when(repository).deleteById(1L);

		service.deleteStudentById(1L);

		verify(repository, times(1)).deleteById(1L);
	}

}
