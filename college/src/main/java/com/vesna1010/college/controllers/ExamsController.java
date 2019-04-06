package com.vesna1010.college.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
import com.vesna1010.college.models.Exam;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.Student;
import com.vesna1010.college.models.StudentSubjectId;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.services.ExamService;
import com.vesna1010.college.services.ProfessorService;
import com.vesna1010.college.services.StudentService;
import com.vesna1010.college.services.SubjectService;

@Controller
@RequestMapping("/exams")
public class ExamsController {

	@Autowired
	private ExamService examService;
	@Autowired
	private ProfessorService professorService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private StudentService studentService;

	@GetMapping(params = "id")
	public ModelAndView renderPageWithExamsByStudyProgramId(@RequestParam Long id,
			@PageableDefault(page = 0, size = 10) @SortDefaults({ @SortDefault(sort = "subject.id", direction = Direction.ASC),
				@SortDefault(sort = "student.id", direction = Direction.ASC) }) Pageable pageable) {
		return new ModelAndView("exams/page", "page", examService.findAllExamsByStudyProgramId(id, pageable));
	}

	@GetMapping(value = "/delete", params = { "id", "subjectId", "studentId" })
	public ModelAndView deleteExamById(@RequestParam Map<String, String> params) {
		Long studentId = Long.valueOf(params.get("studentId"));
		Long subjectId = Long.valueOf(params.get("subjectId"));
		StudentSubjectId id = new StudentSubjectId(studentId, subjectId);

		examService.deleteExamById(id);

		return new ModelAndView("redirect:/exams?id=" + params.get("id"));
	}

	@GetMapping(value = "/form", params = "id")
	@ModelAttribute
	public Exam exam() {
		return new Exam();
	}

	@ModelAttribute("subjects")
	public List<Subject> subjects(@RequestParam Long id,
			@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
					@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return subjectService.findAllSubjectsByStudyProgramId(id, sort);
	}

	@ModelAttribute("students")
	public List<Student> students(@RequestParam Long id,
			@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
					@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return studentService.findAllStudentsByStudyProgramId(id, sort);
	}

	@ModelAttribute("professors")
	public List<Professor> professors(@RequestParam Long id,
			@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
					@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return professorService.findAllProfessorsByStudyProgramId(id, sort);
	}

	@PostMapping(value = "/save", params = "id")
	public ModelAndView saveExam(@RequestParam Long id, @Valid Exam exam, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("exams/form");
		}

		exam = examService.saveExam(exam);

		return new ModelAndView("redirect:/exams/form?id=" + id);
	}

	@GetMapping(value = "/edit", params = { "id", "subjectId", "studentId" })
	public ModelAndView renderFormWithExam(@RequestParam Map<String, String> params) {
		Long studentId = Long.valueOf(params.get("studentId"));
		Long subjectId = Long.valueOf(params.get("subjectId"));
		StudentSubjectId id = new StudentSubjectId(studentId, subjectId);

		return new ModelAndView("exams/form", "exam", examService.findExamById(id));
	}

	@GetMapping(value = "/search", params = "id")
	public ModelAndView renderSearchExamForm(ModelAndView model) {
		return new ModelAndView("exams/search", "exam", new Exam());
	}

	@PostMapping(value = "/search", params = "id")
	public ModelAndView renderSearchedExams(@Valid Exam exam, BindingResult result) {
		if (!result.hasFieldErrors("professor") && !result.hasFieldErrors("subject")
				&& !result.hasFieldErrors("date")) {

			ModelAndView modelAndView = new ModelAndView("exams/searched");
			Subject subject = exam.getSubject();
			Professor professor = exam.getProfessor();
			LocalDate date = exam.getDate();

			modelAndView.addObject("professor", professor);
			modelAndView.addObject("subject", subject);
			modelAndView.addObject("date", date);
			modelAndView.addObject("exams",
					examService.findAllExamsByProfessorAndSubjectAndDate(professor, subject, date));

			return modelAndView;
		}
		return new ModelAndView("exams/search");
	}

}
