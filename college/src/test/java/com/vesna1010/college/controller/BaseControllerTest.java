package com.vesna1010.college.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import com.vesna1010.college.CollegeApplicationTests;

@AutoConfigureMockMvc
public abstract class BaseControllerTest extends CollegeApplicationTests {

	@Autowired
	protected MockMvc mockMvc;

}
