package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="User")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message="Name field is required!")
	@Size(min=2, max=20, message="min 2 and max 20 characters are allowed!")
	private String name;
	
	@Column(unique=true)
	@NotBlank(message="Email is required!")
	@Email(message="Invalid email format")
	private String email;
	
	@NotBlank(message="Password is required!")
	@Size(min=6, message="Password must be atleast 6 characters")
	private String password;
	
	private String role;
	
	private String image;
	
	@AssertTrue(message="You must accept the terms and conditions")
	private Boolean agreement;
	
	@Column(length=500)
	@NotBlank(message="About section is required")
	@Size(min=10, max=500, message="About must be between 10 and 500 characters")
	private String about;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user", orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();

	public User() {
		super();
	}

	public User(int id, String name, String email, String password, 
				String role, String image, Boolean agreement, String about, List<Contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.image = image;
		this.agreement = agreement;
		this.about = about;
		this.contacts = contacts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getAgreement() {
		return agreement;
	}

	public void setAgreement(Boolean agreement) {
		this.agreement = agreement;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", image=" + image + ", agreement=" + agreement + ", about=" + about + ", contacts=" + contacts + "]";
	}
	
}
