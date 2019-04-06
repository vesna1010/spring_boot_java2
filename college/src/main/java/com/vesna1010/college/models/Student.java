package com.vesna1010.college.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.vesna1010.college.enums.Gender;

@Entity
@Table(name = "STUDENTS")
public class Student extends Person {

	private LocalDate startDate;
	private LocalDate endDate;
	private Integer year;
	private StudyProgram studyProgram;
	private Set<Exam> exams = new HashSet<>();

	public Student() {
	}

	public Student(String name, String parent, LocalDate birthDate, String email, String telephone, Gender gender,
			String address, byte[] photo, LocalDate startDate, Integer year, StudyProgram studyProgram) {
		this(null, name, parent, birthDate, email, telephone, gender, address, photo, startDate, year, studyProgram);
	}

	public Student(Long id, String name, String parent, LocalDate birthDate, String email, String telephone,
			Gender gender, String address, byte[] photo, LocalDate startDate, Integer year, StudyProgram studyProgram) {
		super(id, name, parent, birthDate, email, telephone, gender, address, photo);
		this.startDate = startDate;
		this.year = year;
		this.studyProgram = studyProgram;
	}

	@NotNull(message = "{student.startDate}")
	@Column(name = "START_DATE", nullable = false)
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Column(name = "END_DATE", nullable = true)
	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	@NotNull(message = "{student.year}")
	@Min(value = 1, message = "{student.year.min}")
	@Max(value = 5, message = "{student.year.max}")
	@Column(name = "YEAR", nullable = false)
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@NotNull(message = "{student.studyProgram}")
	@ManyToOne
	@JoinColumn(name = "STUDY_PROGRAM_ID")
	public StudyProgram getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(StudyProgram studyProgram) {
		this.studyProgram = studyProgram;
	}

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Exam> getExams() {
		return exams;
	}

	public void setExams(Set<Exam> exams) {
		this.exams = exams;
	}

	@Transient
	public double getAverage() {
		return exams.stream().mapToInt(e -> e.getScore()).average().orElse(0.0);
	}

}
