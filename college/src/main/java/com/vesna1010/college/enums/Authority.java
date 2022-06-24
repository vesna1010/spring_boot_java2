package com.vesna1010.college.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

	PROFESSOR, 
	USER, 
	ADMIN;

	@Override
	public String getAuthority() {
		return this.name();
	}

}
