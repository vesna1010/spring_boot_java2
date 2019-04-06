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
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.services.ProfessorService;
import com.vesna1010.college.services.StudyProgramService;
import com.vesna1010.college.services.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectsController {

	@Autowired
	private SubjectService subjectService;
	@Autowired
	private StudyProgramService studyProgramService;
	@Autowired
	private ProfessorService professorService;

	@GetMapping
	public ModelAndView renderPageWithSubjects(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("subjects/page", "page", subjectService.findAllSubjects(pageable));
	}

	@GetMapping(params = "id")
	public ModelAndView renderPageWithSubjects(@RequestParam Long id,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("subjects/page", "page", subjectService.findAllSubjectsByStudyProgramId(id, pageable));
	}

	@GetMapping("/form")
	@ModelAttribute
	public Subject subject() {
		return new Subject();
	}

	@ModelAttribute("studyPrograms")
	public List<StudyProgram> studyPrograms(@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
			@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return studyProgramService.findAllStudyPrograms(sort);
	}

	@ModelAttribute("professors")
	public List<Professor> professors(@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
			@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return professorService.findAllProfessors(sort);
	}

	@PostMapping("/save")
	public ModelAndView saveSubject(@Valid Subject subject, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("subjects/form");
		}

		subject = subjectService.saveSubject(subject);

		return new ModelAndView("redirect:/subjects/form");
	}

	@GetMapping("/edit")
	public ModelAndView rederFormWithSubject(@RequestParam("id") Subject subject) {
		return new ModelAndView("subjects/form", "subject", subject);
	}

	@GetMapping("/delete")
	public ModelAndView deleteSubjectById(@RequestParam Long id) {
		subjectService.deleteSubjectById(id);

		return new ModelAndView("redirect:/subjects");
	}

}
