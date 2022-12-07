package com.healthcare.api.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.healthcare.api.auth.model.AuthUser;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Role;
import com.healthcare.model.entity.User;
import com.healthcare.model.enums.*;

/**
 * 
 * @author orange
 *
 */
public final class AuthUserFactory {
	private AuthUserFactory() {
	}

	public static AuthUser create(Admin user) {
		return new AuthUser(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
				user.getEmail(), user.getPassword(), mapToGrantedAuthorities(user.getRole()), (user.getStatus() == 1));
	}
	
	public static AuthUser create(User user) {
		return new AuthUser(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
				user.getEmail(), user.getPassword(), mapToGrantedAuthorities(user.getRole()), (user.getStatus() == StatusEnum.REGISTERED));
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
		List<GrantedAuthority> authoritys = new ArrayList<GrantedAuthority>();
		authoritys.add(new SimpleGrantedAuthority(role.getLevelName()));
		return authoritys;
	}
}
