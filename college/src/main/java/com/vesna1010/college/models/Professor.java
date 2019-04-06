package com.vesna1010.college.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import com.vesna1010.college.enums.Gender;

@Entity
@Table(name = "PROFESSORS")
public class Professor extends Person {

	private String title;
	private Set<Subject> subjects = new HashSet<>();
	private Set<Exam> exams = new HashSet<>();

	public Professor() {
	}

	public Professor(String name, String parent, LocalDate birthDate, String email, String telephone, Gender gender,
			String address, byte[] photo, String title) {
		this(null, name, parent, birthDate, email, telephone, gender, address, photo, title);
	}

	public Professor(Long id, String name, String parent, LocalDate birthDate, String email, String telephone,
			Gender gender, String address, byte[] photo, String title) {
		super(id, name, parent, birthDate, email, telephone, gender, address, photo);
		this.title = title;
	}

	@Pattern(regexp = "^[a-zA-Z\\s\\.]{5,}$", message = "{professor.title}")
	@Column(name = "TITLE", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToMany(mappedBy = "professors")
	public Set<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Set<Subject> subjects) {
		this.subjects = subjects;
	}

	@OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Exam> getExams() {
		return exams;
	}

	public void setExams(Set<Exam> exams) {
		this.exams = exams;
	}

}
