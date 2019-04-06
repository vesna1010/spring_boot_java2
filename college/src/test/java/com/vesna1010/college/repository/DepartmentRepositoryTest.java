package com.vesna1010.college.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.vesna1010.college.models.Department;
import com.vesna1010.college.repositories.DepartmentRepository;

public class DepartmentRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private DepartmentRepository repository;

	@Test
	public void findAllDepartmentsWithSortTest() {
		List<Department> departments = repository.findAll(SORT);

		assertThat(departments, hasSize(2));
		assertThat(departments.get(0).getName(), is("Department A"));
		assertThat(departments.get(1).getName(), is("Department B"));
	}

	@Test
	public void findAllDepartmentsWithPageableTest() {
		Page<Department> page = repository.findAll(PAGEABLE);
		List<Department> departments = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(departments, hasSize(2));
		assertThat(departments.get(0).getName(), is("Department B"));
		assertThat(departments.get(1).getName(), is("Department A"));
	}

	@Test
	public void findDepartmentByIdTest() {
		Optional<Department> optional = repository.findById(1L);
		Department department = optional.get();

		assertThat(department.getName(), is("Department B"));
		assertThat(department.getStudyPrograms(), hasSize(2));
	}

	@Test
	public void findDepartmentByIdNotFoundTest() {
		Optional<Department> optional = repository.findById(3L);

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveDepartmentTest() {
		Department department = new Department("Department C", LocalDate.of(2018, Month.SEPTEMBER, 3));

		department = repository.save(department);

		Long id = department.getId();

		assertTrue(repository.existsById(id));
		assertThat(repository.count(), is(3L));
	}

	@Test
	public void updateDepartmentTest() {
		department1.setName("Department");
		department1 = repository.save(department1);

		Optional<Department> optional = repository.findById(1L);
		Department department = optional.get();

		assertThat(department.getName(), is("Department"));
		assertThat(department.getStudyPrograms(), hasSize(2));
		assertThat(repository.count(), is(2L));
	}

	@Test
	public void deleteDepartmentByIdTest() {
		repository.deleteById(1L);

		assertFalse(repository.existsById(1L));
		assertThat(repository.count(), is(1L));
	}

}
