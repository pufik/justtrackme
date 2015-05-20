package com.xdev.template.rest.resource;

public class User extends Resource {
	
	private String email;

	@Override
	public String getUri() {
		return "/users/" + getEmail();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
