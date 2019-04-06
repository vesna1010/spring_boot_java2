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
import com.vesna1010.college.models.Student;
import com.vesna1010.college.repositories.StudentRepository;

public class StudentRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private StudentRepository repository;

	@Test
	public void findAllStudentsByStudyProgramIdWithSortTest() {
		List<Student> students = repository.findAllByStudyProgramId(1L, SORT);

		assertThat(students, hasSize(2));
		assertThat(students.get(0).getName(), is("Student B"));
		assertThat(students.get(1).getName(), is("Student C"));
	}

	@Test
	public void findAllStudentsWithPageableTest() {
		Page<Student> page1 = repository.findAll(PAGEABLE);
		List<Student> students = page1.getContent();

		assertThat(page1.getTotalPages(), is(1));
		assertThat(students, hasSize(3));
		assertThat(students.get(0).getName(), is("Student C"));
		assertThat(students.get(1).getName(), is("Student B"));
		assertThat(students.get(2).getName(), is("Student A"));
	}

	@Test
	public void findAllStudentsByStudyProgramIdWithPageableTest() {
		Page<Student> page1 = repository.findAllByStudyProgramId(1L, PAGEABLE);
		List<Student> students = page1.getContent();

		assertThat(page1.getTotalPages(), is(1));
		assertThat(students, hasSize(2));
		assertThat(students.get(0).getName(), is("Student C"));
		assertThat(students.get(1).getName(), is("Student B"));
	}

	@Test
	public void findStudentByIdTest() {
		Optional<Student> optional = repository.findById(1L);
		Student student = optional.get();

		assertThat(student.getName(), is("Student C"));
		assertThat(student.getExams(), hasSize(2));
	}

	@Test
	public void findStudentByIdNotFoundTest() {
		Optional<Student> optional = repository.findById(4L);

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveStudentTest() {
		Student student = new Student("Student", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.now(), 1, studyProgram1);

		student = repository.save(student);

		Long id = student.getId();

		assertTrue(repository.existsById(id));
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void updateStudentTest() {
		student1.setName("Student");
		student1 = repository.save(student1);

		Optional<Student> optional = repository.findById(1L);
		Student student = optional.get();

		assertThat(student.getName(), is("Student"));
		assertThat(student.getExams(), hasSize(2));
		assertThat(repository.count(), is(3L));
	}

	@Test
	public void deleteStudentByIdTest() {
		repository.deleteById(1L);

		assertFalse(repository.existsById(1L));
		assertThat(repository.count(), is(2L));
	}

}
