package com.vesna1010.college.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "STUDY_PROGRAMS")
public class StudyProgram {

	private Long id;
	private String name;
	private LocalDate createdOn;
	private Integer duration;
	private Department department;
	private Set<Subject> subjects = new HashSet<>();
	private Set<Student> students = new HashSet<>();

	public StudyProgram() {
	}

	public StudyProgram(Long id, String name) {
		this(id, name, null);
	}

	public StudyProgram(Long id, String name, Department department) {
		this(id, name, null, null, department);
	}

	public StudyProgram(String name, LocalDate createdOn, Integer duration, Department department) {
		this(null, name, createdOn, duration, department);
	}

	public StudyProgram(Long id, String name, LocalDate createdOn, Integer duration, Department department) {
		this.id = id;
		this.name = name;
		this.createdOn = createdOn;
		this.duration = duration;
		this.department = department;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Pattern(regexp = "^[a-zA-Z\\s]{5,}$", message = "{studyProgram.name}")
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull(message = "{studyProgram.createdOn}")
	@Column(name = "CREATED_ON", nullable = false)
	public LocalDate getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}

	@NotNull(message = "{studyProgram.duration}")
	@Min(value = 1, message = "{studyProgram.duration.min}")
	@Max(value = 5, message = "{studyProgram.duration.max}")
	@Column(name = "DURATION", nullable = false)
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@NotNull(message = "{studyProgram.department}")
	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@OneToMany(mappedBy = "studyProgram", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Set<Subject> subjects) {
		this.subjects = subjects;
	}

	@OneToMany(mappedBy = "studyProgram", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudyProgram other = (StudyProgram) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
