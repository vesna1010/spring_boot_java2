package com.vesna1010.college.models;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "SUBJECTS")
public class Subject {

	private Long id;
	private String name;
	private StudyProgram studyProgram;
	private Set<Professor> professors = new HashSet<>();
	private Set<Exam> exams = new HashSet<>();

	public Subject() {
	}

	public Subject(String name, StudyProgram studyProgram,  Set<Professor> professors) {
		this(null, name, studyProgram, professors);
	}

	public Subject(Long id, String name, StudyProgram studyProgram,  Set<Professor> professors) {
		this.id = id;
		this.name = name;
		this.studyProgram = studyProgram;
		this.professors = professors;
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

	@Pattern(regexp = "^[a-zA-Z0-9\\s]{5,}$", message = "{subject.name}")
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull(message = "{subject.studyProgram}")
	@ManyToOne
	@JoinColumn(name = "STUDY_PROGRAM_ID")
	public StudyProgram getStudyProgram() {
		return studyProgram;
	}

	public void setStudyProgram(StudyProgram studyProgram) {
		this.studyProgram = studyProgram;
	}

	@NotEmpty(message = "{subject.professors}")
	@ManyToMany
	@JoinTable(
			name = "SUBJECTS_PROFESSORS", 
			joinColumns = @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID"), 
			inverseJoinColumns = @JoinColumn(name = "PROFESSOR_ID", referencedColumnName = "ID"))
	public Set<Professor> getProfessors() {
		return professors;
	}

	public void setProfessors(Set<Professor> professors) {
		this.professors = professors;
	}

	@OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Exam> getExams() {
		return exams;
	}

	public void setExams(Set<Exam> exams) {
		this.exams = exams;
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
		Subject other = (Subject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
