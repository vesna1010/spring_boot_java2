package com.vesna1010.college.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.college.enums.Gender;
import com.vesna1010.college.models.Student;
import com.vesna1010.college.services.StudentService;
import com.vesna1010.college.services.StudyProgramService;

public class StudentsControllerTest extends BaseControllerTest {

	@MockBean
	private StudentService studentService;
	@MockBean
	private StudyProgramService studyProgramService;

	@Test
	@WithAnonymousUser
	public void renderPageWithStudentsWithAnonymousUserTest() throws Exception {
		renderPageWithStudentsNotLoggedIn();
	}
	
	private void renderPageWithStudentsNotLoggedIn() throws Exception {
		when(studentService.findAllStudents(PAGEABLE)).thenReturn(
				new PageImpl<Student>(Arrays.asList(student1, student2, student3)));
	
		mockMvc.perform(get("/students"))
	           .andExpect(status().is3xxRedirection())
	           .andExpect(redirectedUrlPattern("**/login"));
		
		verify(studentService, times(0)).findAllStudents(PAGEABLE);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithStudentsWithAuthorityProfessorTest() throws Exception {
		renderPageWithStudents();
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithStudentsWithAuthorityUserTest() throws Exception {
		renderPageWithStudents();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithStudentsWithAuthorityAdminTest() throws Exception {
		renderPageWithStudents();
	}
	
	private void renderPageWithStudents() throws Exception {
		when(studentService.findAllStudents(PAGEABLE)).thenReturn(
				new PageImpl<Student>(Arrays.asList(student1, student2, student3)));
	
		mockMvc.perform(get("/students"))
		       .andExpect(status().isOk())
               .andExpect(model().attribute("page", hasProperty("content", hasSize(3))))
               .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
               .andExpect(view().name("students/page"));
		
		verify(studentService, times(1)).findAllStudents(PAGEABLE);
	}

	@Test
	@WithAnonymousUser
	public void renderPageWithStudentsByStudyProgramIdWithAnonymousUserTest() throws Exception {
		renderPageWithStudentsByStudyProgramIdNotLoggedIn();
	}
	
	private void renderPageWithStudentsByStudyProgramIdNotLoggedIn() throws Exception {
		when(studentService.findAllStudentsByStudyProgramId(1L, PAGEABLE)).thenReturn(
				new PageImpl<Student>(Arrays.asList(student1, student2)));
	
		mockMvc.perform(
				get("/students")
				.param("id", "1")
				)
	           .andExpect(status().is3xxRedirection())
	           .andExpect(redirectedUrlPattern("**/login"));
		
		verify(studentService, times(0)).findAllStudentsByStudyProgramId(1L, PAGEABLE);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithStudentsByStudyProgramIdWithAuthorityProfessorTest() throws Exception {
		renderPageWithStudentsByStudyProgramId();
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithStudentsByStudyProgramIdWithAuthorityUserTest() throws Exception {
		renderPageWithStudentsByStudyProgramId();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithStudentsByStudyProgramIdWithAuthorityAdminTest() throws Exception {
		renderPageWithStudentsByStudyProgramId();
	}
	
	private void renderPageWithStudentsByStudyProgramId() throws Exception {
		when(studentService.findAllStudentsByStudyProgramId(1L, PAGEABLE)).thenReturn(
				new PageImpl<Student>(Arrays.asList(student1, student2)));
	
		mockMvc.perform(
				get("/students")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
               .andExpect(model().attribute("page", hasProperty("content", hasSize(2))))
               .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
               .andExpect(view().name("students/page"));
		
		verify(studentService, times(1)).findAllStudentsByStudyProgramId(1L, PAGEABLE);
	}

	@Test
	@WithAnonymousUser
	public void renderEmptyFormWithAnonymousUserTest() throws Exception {
		renderEmptyFormNotLoggedIn();
	}
	
	private void renderEmptyFormNotLoggedIn() throws Exception {
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		
		mockMvc.perform(get("/students/form"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(studyProgramService, times(0)).findAllStudyPrograms(SORT);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderEmptyFormWithAuthorityProfessorTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}
	
	private void renderEmptyFormNotAuthorized() throws Exception {
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		
		mockMvc.perform(get("/students/form"))
		       .andExpect(status().isForbidden());
		
		verify(studyProgramService, times(0)).findAllStudyPrograms(SORT);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderEmptyFormWithAuthorityUserTest() throws Exception {
		renderEmptyForm();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderEmptyFormWithAuthorityAdminTest() throws Exception {
		renderEmptyForm();
	}
	
	private void renderEmptyForm() throws Exception {
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		
		mockMvc.perform(get("/students/form"))
		       .andExpect(status().isOk())
               .andExpect(model().attribute("student", is(new Student())))
               .andExpect(model().attribute("studyPrograms", hasSize(3)))
               .andExpect(view().name("students/form"));
		
		verify(studyProgramService, times(1)).findAllStudyPrograms(SORT);
	}
	
	@Test
	@WithAnonymousUser
	public void saveStudentWithAnonymousUserTest() throws Exception {
		saveStudentNotLoggedIn();
	}
	
	private void saveStudentNotLoggedIn() throws Exception {
		Student student = new Student("Student", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram1);
		
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		when(studentService.saveStudent(student)).thenReturn(new Student(4L,"Student", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram1));
			
		mockMvc.perform(
				post("/students/save")
				.param("name", "Student")
				.param("parent", "Parent")
				.param("birthDate", "06.01.1995")
				.param("email", "student@gmail.com")
				.param("telephone", "065 123 333")
				.param("gender", "MALE")
				.param("address", "Address")
				.param("photo", student.getPhoto().toString())
				.param("startDate", "01.01.2019")
				.param("year", "1")
				.param("studyProgram", "1")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
        verify(studyProgramService, times(0)).findStudyProgramById(1L);
        verify(studentService, times(0)).saveStudent(student);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void saveStudentWithAuthorityProfessorTest() throws Exception {
		saveStudentNotAuthorized();
	}
	
	private void saveStudentNotAuthorized() throws Exception {
		Student student = new Student("Student", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram1);
		
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		when(studentService.saveStudent(student)).thenReturn(new Student(4L,"Student", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram1));
			
		mockMvc.perform(
				post("/students/save")
				.param("name", "Student")
				.param("parent", "Parent")
				.param("birthDate", "06.01.1995")
				.param("email", "student@gmail.com")
				.param("telephone", "065 123 333")
				.param("gender", "MALE")
				.param("address", "Address")
				.param("photo", student.getPhoto().toString())
				.param("startDate", "01.01.2019")
				.param("year", "1")
				.param("studyProgram", "1")
				.with(csrf())
				)
		       .andExpect(status().isForbidden());
		
        verify(studyProgramService, times(0)).findStudyProgramById(1L);
        verify(studentService, times(0)).saveStudent(student);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveStudentWithAauthorityUserTest() throws Exception {
		saveStudent();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void saveStudentWithAuthorityAdminTest() throws Exception {
		saveStudent();
	}

	private void saveStudent() throws Exception {
		Student student = new Student("Student", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram1);
		
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		when(studentService.saveStudent(student)).thenReturn(new Student(4L,"Student", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram1));
			
		mockMvc.perform(
				post("/students/save")
				.param("name", "Student")
				.param("parent", "Parent")
				.param("birthDate", "06.01.1995")
				.param("email", "student@gmail.com")
				.param("telephone", "065 123 333")
				.param("gender", "MALE")
				.param("address", "Address")
				.param("photo", student.getPhoto().toString())
				.param("startDate", "01.01.2019")
				.param("year", "1")
				.param("studyProgram", "1")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/students/form"));;
		
        verify(studyProgramService, times(1)).findStudyProgramById(1L);
        verify(studentService, times(1)).saveStudent(student);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void saveStudentInvalidFormTest() throws Exception {
		Student student = new Student("Student ??", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram1);
		
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		when(studentService.saveStudent(student)).thenReturn(new Student(4L,"Student ??", "Parent", LocalDate.of(1995, Month.JANUARY, 6), "student@gmail.com",
				"065 123 333", Gender.MALE, "Address", getPhoto(), LocalDate.of(2019, Month.JANUARY, 1), 1, studyProgram1));
			
		mockMvc.perform(
				post("/students/save")
				.param("name", "Student ??")
				.param("parent", "Parent")
				.param("birthDate", "06.01.1995")
				.param("email", "student@gmail.com")
				.param("telephone", "065 123 333")
				.param("gender", "MALE")
				.param("address", "Address")
				.param("photo", student.getPhoto().toString())
				.param("startDate", "01.01.2019")
				.param("year", "1")
				.param("studyProgram", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
               .andExpect(model().attributeHasFieldErrors("student", "name"))
               .andExpect(model().attribute("student", hasProperty("id", is(nullValue()))))
               .andExpect(model().attribute("studyPrograms", hasSize(3)))
               .andExpect(view().name("students/form"));;
		
        verify(studyProgramService, times(1)).findAllStudyPrograms(SORT);
        verify(studyProgramService, times(1)).findStudyProgramById(1L);
        verify(studentService, times(0)).saveStudent(student);
	}

	@Test
	@WithAnonymousUser
	public void rederFormWithStudentWithAnonymousUserTest() throws Exception {
		rederFormWithStudentNotLoggedIn();
	}
	
	private void rederFormWithStudentNotLoggedIn() throws Exception {
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		when(studentService.findStudentById(1L)).thenReturn(student1);
		
		mockMvc.perform(
				get("/students/edit")
				.param("id", "1")
				)
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(studyProgramService, times(0)).findAllStudyPrograms(SORT);
		verify(studentService, times(0)).findStudentById(1L);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void rederFormWithStudentWithAuthorityProfessorTest() throws Exception {
		rederFormWithStudentNotAuthorized();
	}
	
	private void rederFormWithStudentNotAuthorized() throws Exception {
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		when(studentService.findStudentById(1L)).thenReturn(student1);
		
		mockMvc.perform(
				get("/students/edit")
				.param("id", "1")
				)
               .andExpect(status().isForbidden());
		
		verify(studyProgramService, times(0)).findAllStudyPrograms(SORT);
		verify(studentService, times(0)).findStudentById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void rederFormWithStudentWithAuthorityUserTest() throws Exception {
		rederFormWithStudent();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void rederFormWithStudentWithAuthorityAdminTest() throws Exception {
		rederFormWithStudent();
	}
	
	private void rederFormWithStudent() throws Exception {
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		when(studentService.findStudentById(1L)).thenReturn(student1);
		
		mockMvc.perform(
				get("/students/edit")
				.param("id", "1")
				)
		      .andExpect(status().isOk())
              .andExpect(model().attribute("student", hasProperty("name", is("Student C"))))
              .andExpect(model().attribute("studyPrograms", hasSize(3)))
              .andExpect(view().name("students/form"));
		
		verify(studyProgramService, times(1)).findAllStudyPrograms(SORT);
		verify(studentService, times(1)).findStudentById(1L);
	}

	@Test
	@WithAnonymousUser
	public void deleteStudentByIdWithAnonymousUserTest() throws Exception {
		deleteStudentByIdNotLoggedIn();
	}
	
	private void deleteStudentByIdNotLoggedIn() throws Exception {
		doNothing().when(studentService).deleteStudentById(1L);
		
		mockMvc.perform(
				get("/students/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
	
		verify(studentService, times(0)).deleteStudentById(1L);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void deleteStudentByIdWithAuthorityProfessorTest() throws Exception {
		deleteStudentByIdNotAuthorized();
	}

	private void deleteStudentByIdNotAuthorized() throws Exception {
		doNothing().when(studentService).deleteStudentById(1L);
		
		mockMvc.perform(
				get("/students/delete")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	
		verify(studentService, times(0)).deleteStudentById(1L);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteStudentByIdWithAuthorityUserTest() throws Exception {
		deleteStudentById();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteStudentByIdWithAuthorityAdminTest() throws Exception {
		deleteStudentById();
	}
	
	private void deleteStudentById() throws Exception {
		doNothing().when(studentService).deleteStudentById(1L);
		
		mockMvc.perform(
				get("/students/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/students"));
	
		verify(studentService, times(1)).deleteStudentById(1L);
	}

	@Test
	@WithAnonymousUser
	public void rederStudentExamsWithAnonymousUserTest() throws Exception {
		rederStudentExamsNotLoggedIn();
	}
	
	private void rederStudentExamsNotLoggedIn() throws Exception {
		when(studentService.findStudentById(1L)).thenReturn(student1);
		
		mockMvc.perform(
				get("/students/exams")
				.param("id", "1")
				)
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(studentService, times(0)).findStudentById(1L);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void rederStudentExamsWithAuthorityProfessorTest() throws Exception {
		rederStudentExams();
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void rederStudentExamsWithAuthorityUserTest() throws Exception {
		rederStudentExams();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void rederStudentExamsWithAuthorityAdminTest() throws Exception {
		rederStudentExams();
	}
	
	private void rederStudentExams() throws Exception {
		when(studentService.findStudentById(1L)).thenReturn(student1);
		
		mockMvc.perform(
				get("/students/exams")
				.param("id", "1")
				)
		      .andExpect(status().isOk())
              .andExpect(model().attribute("student", hasProperty("name", is("Student C"))))
              .andExpect(view().name("students/exams"));
		
		verify(studentService, times(1)).findStudentById(1L);
	}

}
