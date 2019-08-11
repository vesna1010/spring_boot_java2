package com.vesna1010.college.repository;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import com.vesna1010.college.BaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:init/MySQL.sql")
public abstract class BaseRepositoryTest extends BaseTest {

}
