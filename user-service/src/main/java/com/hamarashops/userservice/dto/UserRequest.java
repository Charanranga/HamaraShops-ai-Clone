package com.hamarashops.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

	@NotBlank(message = "Full name is required")
	@Size(min = 2, max = 150, message = "Full name must be between 2 and 150 characters")
	private String fullName;

	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	private String email;

	private String phone;

	private String address;

	public UserRequest() {
		super();
	}

	public UserRequest(String fullName, String email, String phone, String address) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
