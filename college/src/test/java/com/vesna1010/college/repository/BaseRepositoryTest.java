package com.vesna1010.college.repository;

import org.springframework.test.context.jdbc.Sql;
import com.vesna1010.college.CollegeApplicationTests;

@Sql(scripts = "classpath:init/MySQL.sql")
public abstract class BaseRepositoryTest extends CollegeApplicationTests {

}
