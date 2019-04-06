package com.vesna1010.college.controllers;

import java.security.Principal;
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
import com.vesna1010.college.models.User;
import com.vesna1010.college.services.UserService;
import com.vesna1010.college.validators.UserValidator;

@Controller
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService service;
	@Autowired
	private UserValidator validator;

	@GetMapping
	public ModelAndView renderPageWitUsers(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return new ModelAndView("users/page", "page", service.findAllUsers(pageable));
	}

	@GetMapping("/form")
	@ModelAttribute
	public User user() {
		return new User();
	}

	@PostMapping("/save")
	public ModelAndView saveUser(User user, BindingResult errors) {
		validator.validate(user, errors);

		if (errors.hasErrors()) {
			return new ModelAndView("users/form");
		}

		user = service.saveUser(user);

		return new ModelAndView("redirect:/users/form");
	}

	@RequestMapping("/delete")
	public ModelAndView deleteUserById(@RequestParam Long id) {
		service.deleteUserById(id);

		return new ModelAndView("redirect:/users");
	}

	@RequestMapping("/disable")
	public ModelAndView disableUserById(@RequestParam Long id) {
		service.disableUserById(id);

		return new ModelAndView("redirect:/users");
	}

	@RequestMapping("/edit")
	public ModelAndView renderFormWithUser(Principal principal) {
		return new ModelAndView("users/update-password-form", "user", service.findUserByEmail(principal.getName()));
	}

	@PostMapping("/update")
	public ModelAndView updatePassword(User user, BindingResult errors) {
		validator.validate(user, errors);

		if (errors.hasFieldErrors("password") || errors.hasFieldErrors("confirmPassword")) {
			return new ModelAndView("users/update-password-form");
		}

		return new ModelAndView("users/update-password-form", "user", service.saveUser(user));
	}

}
