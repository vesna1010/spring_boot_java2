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
import com.vesna1010.college.models.Department;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.services.DepartmentService;
import com.vesna1010.college.services.StudyProgramService;

@Controller
@RequestMapping("/study_programs")
public class StudyProgramsController {

	@Autowired
	private StudyProgramService studyProgramService;
	@Autowired
	private DepartmentService departmentService;

	@GetMapping
	public ModelAndView renderPageWithStudyPrograms(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("study_programs/page", "page", studyProgramService.findAllStudyPrograms(pageable));
	}

	@GetMapping(params = "id")
	public ModelAndView renderPageWithStudyPrograms(@RequestParam Long id,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("study_programs/page", "page",
				studyProgramService.findAllStudyProgramsByDepartmentId(id, pageable));
	}

	@GetMapping("/form")
	@ModelAttribute
	public StudyProgram studyProgram() {
		return new StudyProgram();
	}

	@ModelAttribute("departments")
	public List<Department> departments(@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
			@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return departmentService.findAllDepartments(sort);
	}

	@PostMapping("/save")
	public ModelAndView saveStudyProgram(@Valid StudyProgram studyProgram, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("study_programs/form");
		}

		studyProgram = studyProgramService.saveStudyProgram(studyProgram);

		return new ModelAndView("redirect:/study_programs/form");
	}

	@GetMapping("/edit")
	public ModelAndView rederFormWithStudyProgram(@RequestParam("id") StudyProgram studyProgram) {
		return new ModelAndView("study_programs/form", "studyProgram", studyProgram);
	}

	@GetMapping("/details")
	public ModelAndView rederStudyProgramDetails(@RequestParam("id") StudyProgram studyProgram) {
		return new ModelAndView("study_programs/details", "studyProgram", studyProgram);
	}

	@GetMapping("/delete")
	public ModelAndView deleteStudyProgramById(@RequestParam Long id) {
		studyProgramService.deleteStudyProgramById(id);

		return new ModelAndView("redirect:/study_programs");
	}

}
