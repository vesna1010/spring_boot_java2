package com.vesna1010.college.models;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Base64;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;
import com.vesna1010.college.enums.Gender;

@MappedSuperclass
public abstract class Person {

	private Long id;
	private String name;
	private String parent;
	private LocalDate birthDate;
	private String email;
	private String telephone;
	private Gender gender;
	private String address;
	private byte[] photo;

	public Person() {
	}

	public Person(Long id, String name, String parent, LocalDate birthDate, String email, String telephone,
			Gender gender, String address, byte[] photo) {
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.birthDate = birthDate;
		this.email = email;
		this.telephone = telephone;
		this.gender = gender;
		this.address = address;
		this.photo = photo;
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

	@Pattern(regexp = "^[a-zA-Z\\s]{5,}$", message = "{person.name}")
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Pattern(regexp = "^[a-zA-Z\\s]{5,}$", message = "{person.parent}" )
	@Column(name = "PARENT", nullable = false)
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	@NotNull(message = "{person.birthDate}")
	@Column(name = "BIRTH_DATE", nullable = false)
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@NotBlank(message = "{person.email.blank}")
	@Email(message = "{person.email.invalid}")
	@Column(name = "EMAIL", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Pattern(regexp = "^(((00|\\+)\\d{3})|0)\\s?\\d{2}\\s?\\d{3}\\s?\\d{3,4}$", message = "{person.telephone}")
	@Column(name = "TELEPHONE", nullable = false)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@NotNull(message = "{person.gender}")
	@Enumerated(EnumType.STRING)
	@Column(name = "GENDER", nullable = false)
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Pattern(regexp = "^[a-zA-Z0-9\\s\\,]+$", message = "{person.address}")
	@Column(name = "ADDRESS", nullable = false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@NotNull(message = "{person.photo}")
	@Lob
	@Column(name = "PHOTO", nullable = false)
	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	public void setFile(MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				photo = file.getBytes();
			} catch (IOException e) {
			}
		}
	}
	
	@Transient
	public MultipartFile getFile() {
		return null;
	}

	@Override
	public String toString() {
		try {
			return new String(Base64.getEncoder().encode(this.photo), "UTF-8");
		} catch (UnsupportedEncodingException | NullPointerException e) {
			return "";
		}
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
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
