package com.vesna1010.college.models;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EXAMS")
@IdClass(StudentSubjectId.class)
public class Exam {

	private LocalDate date;
	private Student student;
	private Subject subject;
	private Professor professor;
	private Integer score;

	public Exam() {
	}

	public Exam(LocalDate date, Student student, Subject subject, Professor professor, Integer score) {
		this.date = date;
		this.student = student;
		this.subject = subject;
		this.professor = professor;
		this.score = score;
	}

	@NotNull(message = "{exam.date}")
	@Column(name = "DATE", nullable = false)
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@NotNull(message = "{exam.student}")
	@Id
	@ManyToOne
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@NotNull(message = "{exam.subject}")
	@Id
	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID")
	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	@NotNull(message = "{exam.professor}")
	@ManyToOne
	@JoinColumn(name = "PROFESSOR_ID")
	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	@NotNull(message = "{exam.score}")
	@Min(value = 6, message = "{exam.score.min}")
	@Max(value = 10, message = "{exam.score.max}")
	@Column(name = "SCORE", nullable = false)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((student == null) ? 0 : student.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		Exam other = (Exam) obj;
		if (student == null) {
			if (other.student != null)
				return false;
		} else if (!student.equals(other.student))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

}
