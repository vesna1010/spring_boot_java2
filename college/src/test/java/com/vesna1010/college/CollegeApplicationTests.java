package com.vesna1010.college;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.junit4.SpringRunner;
import com.vesna1010.college.enums.Authority;
import com.vesna1010.college.enums.Gender;
import com.vesna1010.college.models.Department;
import com.vesna1010.college.models.Exam;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.Student;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.models.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class CollegeApplicationTests {

	public static final Sort SORT;
	public static final Pageable PAGEABLE;

	protected Department department1;
	protected Department department2;

	protected StudyProgram studyProgram1;
	protected StudyProgram studyProgram2;
	protected StudyProgram studyProgram3;

	protected Subject subject1;
	protected Subject subject2;
	protected Subject subject3;
	protected Subject subject4;

	protected Professor professor1;
	protected Professor professor2;
	protected Professor professor3;

	protected Student student1;
	protected Student student2;
	protected Student student3;

	protected Exam exam1;
	protected Exam exam2;
	protected Exam exam3;
	protected Exam exam4;

	protected User user1;
	protected User user2;
	protected User user3;

	static {
		SORT = Sort.by(Order.asc("name"), Order.asc("id"));
		PAGEABLE = PageRequest.of(0, 10, Direction.ASC, "id");
	}

	{
		department1 = new Department(1L, "Department B", LocalDate.of(2018, Month.SEPTEMBER, 1));
		department2 = new Department(2L, "Department A", LocalDate.of(2018, Month.SEPTEMBER, 2));

		studyProgram1 = new StudyProgram(1L, "Study Program B", LocalDate.of(2018, Month.SEPTEMBER, 1), 3, department1);

		studyProgram2 = new StudyProgram(2L, "Study Program C", LocalDate.of(2018, Month.SEPTEMBER, 2), 3, department2);

		studyProgram3 = new StudyProgram(3L, "Study Program A", LocalDate.of(2018, Month.SEPTEMBER, 1), 3, department1);

		professor1 = new Professor(1L, "Professor B", "Parent B", LocalDate.of(1972, Month.DECEMBER, 11),
				"professorB@gmail.com", "065 123 123", Gender.MALE, "Address B", getPhoto(), "Title B");

		professor2 = new Professor(1L, "Professor C", "Parent C", LocalDate.of(1975, Month.MAY, 23),
				"professorC@gmail.com", "065 123 124", Gender.MALE, "Address C", getPhoto(), "Title C");

		professor3 = new Professor(1L, "Professor A", "Parent A", LocalDate.of(1978, Month.AUGUST, 29),
				"professorA@gmail.com", "065 123 125", Gender.MALE, "Address A", getPhoto(), "Title A");

		subject1 = new Subject(1L, "Subject B", studyProgram1,
				new HashSet<Professor>(Arrays.asList(professor1, professor2)));
		subject2 = new Subject(2L, "Subject D", studyProgram2,
				new HashSet<Professor>(Arrays.asList(professor2, professor3)));
		subject3 = new Subject(3L, "Subject A", studyProgram1, new HashSet<Professor>(Arrays.asList(professor1)));
		subject4 = new Subject(4L, "Subject C", studyProgram3, new HashSet<Professor>(Arrays.asList(professor3)));

		student1 = new Student(1L, "Student C", "Parent C", LocalDate.of(1995, Month.DECEMBER, 22),
				"studentC@gmail.com", "065 312 222", Gender.MALE, "Address C", getPhoto(),
				LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram1);

		student2 = new Student(2L, "Student B", "Parent B", LocalDate.of(1995, Month.OCTOBER, 2), "studentB@gmail.com",
				"065 314 252", Gender.MALE, "Address B", getPhoto(), LocalDate.of(2019, Month.JANUARY, 1), 1,
				studyProgram1);

		student3 = new Student(3L, "Student A", "Parent A", LocalDate.of(1995, Month.SEPTEMBER, 11),
				"studentA@gmail.com", "065 812 220", Gender.MALE, "Address A", getPhoto(),
				LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram2);

		exam1 = new Exam(LocalDate.of(2019, Month.FEBRUARY, 10), student1, subject1, professor1, 8);
		exam2 = new Exam(LocalDate.of(2019, Month.FEBRUARY, 13), student1, subject3, professor1, 9);
		exam3 = new Exam(LocalDate.of(2019, Month.FEBRUARY, 10), student2, subject1, professor2, 7);
		exam4 = new Exam(LocalDate.of(2019, Month.FEBRUARY, 10), student3, subject2, professor2, 7);

		user1 = new User(1L, "User A", "userA@gmail.com", "PasswordA", Authority.ADMIN);
		user2 = new User(2L, "User B", "userB@gmail.com", "PasswordB", Authority.USER);
		user3 = new User(3L, "User C", "userC@gmail.com", "PasswordC", Authority.PROFESSOR);
	}

	public byte[] getPhoto() {
		File file = new File("src\\test\\java\\image\\image.jpg");
		byte[] photo = null;

		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			photo = new byte[(int) file.length()];
			is.read(photo);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return photo;
	}

}
