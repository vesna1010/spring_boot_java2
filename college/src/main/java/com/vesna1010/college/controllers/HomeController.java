package com.vesna1010.college.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.vesna1010.college.services.DepartmentService;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private DepartmentService service;

	@GetMapping
	public ModelAndView renderHomePage(@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
			@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return new ModelAndView("home", "departments", service.findAllDepartments(sort));
	}

}
