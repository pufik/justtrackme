package com.justtrackme.model;

public enum UserRole {

	ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN");

	private final String securityCode;

	private UserRole(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getSecurityCode() {
		return securityCode;
	}
}
