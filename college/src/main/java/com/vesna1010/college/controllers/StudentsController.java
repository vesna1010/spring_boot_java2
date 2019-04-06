package com.vesna1010.college.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.vesna1010.college.models.Student;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.services.StudentService;
import com.vesna1010.college.services.StudyProgramService;

@Controller
@RequestMapping("/students")
public class StudentsController {

	@Autowired
	private StudyProgramService studyProgramService;
	@Autowired
	private StudentService studentsService;

	@GetMapping
	public ModelAndView renderPageWithStudents(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("students/page", "page", studentsService.findAllStudents(pageable));
	}

	@GetMapping(params = "id")
	public ModelAndView renderPageWithStudents(@RequestParam Long id,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("students/page", "page", studentsService.findAllStudentsByStudyProgramId(id, pageable));
	}

	@GetMapping("/form")
	@ModelAttribute
	public Student student() {
		return new Student();
	}

	@ModelAttribute("studyPrograms")
	public List<StudyProgram> studyPrograms(@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
			@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return studyProgramService.findAllStudyPrograms(sort);
	}

	@PostMapping("/save")
	public ModelAndView saveStudent(@Valid Student student, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("students/form");
		}

		student = studentsService.saveStudent(student);

		return new ModelAndView("redirect:/students/form");
	}

	@GetMapping("/edit")
	public ModelAndView rederFormWithStudent(@RequestParam("id") Student student) {
		return new ModelAndView("students/form", "student", student);
	}

	@GetMapping("/exams")
	public ModelAndView rederStudentExams(@RequestParam("id") Student student) {
		return new ModelAndView("students/exams", "student", student);
	}

	@GetMapping("/delete")
	public ModelAndView deleteStudentById(@RequestParam Long id) {
		studentsService.deleteStudentById(id);

		return new ModelAndView("redirect:/students");
	}

}
