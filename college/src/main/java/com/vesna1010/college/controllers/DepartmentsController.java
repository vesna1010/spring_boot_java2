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
import com.vesna1010.college.models.Department;
import com.vesna1010.college.services.DepartmentService;

@Controller
@RequestMapping("/departments")
public class DepartmentsController {

	@Autowired
	private DepartmentService service;

	@GetMapping
	public ModelAndView renderPageWithDepartments(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("departments/page", "page", service.findAllDepartments(pageable));
	}

	@GetMapping("/form")
	@ModelAttribute
	public Department department() {
		return new Department();
	}

	@PostMapping("/save")
	public ModelAndView saveDepartment(@Valid Department department, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("departments/form");
		}

		department = service.saveDepartment(department);

		return new ModelAndView("redirect:/departments/form");
	}

	@GetMapping("/edit")
	public ModelAndView renderFormWithDepartment(@RequestParam("id") Department department) {
		return new ModelAndView("departments/form", "department", department);
	}

	@GetMapping("/delete")
	public ModelAndView deleteDepartmentById(@RequestParam Long id) {
		service.deleteDepartmentById(id);

		return new ModelAndView("redirect:/departments");
	}

}
