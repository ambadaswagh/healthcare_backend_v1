package com.healthcare.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.User;
import com.healthcare.repository.AdminRepository;
import com.healthcare.repository.UserRepository;

/**
 * 
 * @author orange
 *
 */

@Service
public class AuthUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Admin userAdmin = adminRepository.findByUsername(userName);
		if (userAdmin == null) {
			User user = userRepository.findByUsername(userName);
			if(user==null){
				throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userName));
			}else{
				return AuthUserFactory.create(user);
			}
		} else {
			return AuthUserFactory.create(userAdmin);
		}
	}
}
