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
		when(repository.findAllByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student2, student1));

		List<Student> students = service.findAllStudentsByStudyProgramId(1L, SORT);

		assertThat(students, hasSize(2));
		assertThat(students.get(0).getName(), is("Student B"));
		assertThat(students.get(1).getName(), is("Student C"));
		verify(repository, times(1)).findAllByStudyProgramId(1L, SORT);
	}

	@Test
	public void findAllStudentsWithPageableTest() {
		when(repository.findAll(PAGEABLE))
				.thenReturn(new PageImpl<Student>(Arrays.asList(student1, student2, student3)));

		Page<Student> page = service.findAllStudents(PAGEABLE);
		List<Student> students = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(students, hasSize(3));
		assertThat(students.get(0).getName(), is("Student C"));
		assertThat(students.get(1).getName(), is("Student B"));
		assertThat(students.get(2).getName(), is("Student A"));
		verify(repository, times(1)).findAll(PAGEABLE);
	}

	@Test
	public void findAllStudentsByStudyProgramIdWithPageableTest() {
		when(repository.findAllByStudyProgramId(1L, PAGEABLE))
				.thenReturn(new PageImpl<Student>(Arrays.asList(student1, student2)));

		Page<Student> page = service.findAllStudentsByStudyProgramId(1L, PAGEABLE);
		List<Student> students = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(students, hasSize(2));
		assertThat(students.get(0).getName(), is("Student C"));
		assertThat(students.get(1).getName(), is("Student B"));
		verify(repository, times(1)).findAllByStudyProgramId(1L, PAGEABLE);
	}

	@Test
	public void findStudentByIdTest() {
		when(repository.findById(1L)).thenReturn(Optional.of(student1));

		Student student = service.findStudentById(1L);

		assertThat(student.getName(), is("Student C"));
		verify(repository, times(1)).findById(1L);
	}

	@Test(expected = RuntimeException.class)
	public void findStudentByIdNotFoundTest() {
		when(repository.findById(4L)).thenReturn(Optional.empty());

		service.findStudentById(4L);
	}

	@Test
	public void saveStudentTest() {
		Student student = new Student("Student", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.now(), 1, studyProgram1);

		when(repository.save(student)).thenReturn(
				new Student(4L, "Student", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
						"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.now(), 1, studyProgram1));

		Student studentSaved = service.saveStudent(student);

		assertThat(studentSaved.getId(), is(4L));
		verify(repository, times(1)).save(student);
	}

	@Test
	public void deleteStudentByIdTest() {
		doNothing().when(repository).deleteById(1L);

		service.deleteStudentById(1L);

		verify(repository, times(1)).deleteById(1L);
	}

}
