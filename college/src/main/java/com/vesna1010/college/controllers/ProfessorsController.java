package com.vesna1010.college.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.services.ProfessorService;

@Controller
@RequestMapping("/professors")
public class ProfessorsController {

	@Autowired
	private ProfessorService professorsService;

	@GetMapping
	public ModelAndView renderPageWithProfessors(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("professors/page", "page", professorsService.findAllProfessors(pageable));
	}

	@GetMapping(params = "id")
	public ModelAndView renderPageWithProfessors(@RequestParam Long id,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("professors/page", "page",
				professorsService.findAllProfessorsByStudyProgramId(id, pageable));
	}

	@GetMapping("/form")
	@ModelAttribute
	public Professor professor() {
		return new Professor();
	}

	@PostMapping("/save")
	public ModelAndView saveProfessor(@Valid Professor professor, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("professors/form");
		}

		professor = professorsService.saveProfessor(professor);

		return new ModelAndView("redirect:/professors/form");
	}

	@GetMapping("/edit")
	public ModelAndView rederFormWithProfessor(@RequestParam("id") Professor professor) {
		return new ModelAndView("professors/form", "professor", professor);
	}

	@GetMapping("/delete")
	public ModelAndView deleteProfessorById(@RequestParam Long id) {
		professorsService.deleteProfessorById(id);

		return new ModelAndView("redirect:/professors");
	}

}
