package com.healthcare.service.impl;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Password;
import com.healthcare.model.entity.User;
import com.healthcare.repository.AdminRepository;
import com.healthcare.repository.PasswordRepository;
import com.healthcare.service.AdminService;
import com.healthcare.service.EmailSenderService;
import com.healthcare.service.PasswordService;
import com.healthcare.service.UserService;

@Service
public class PasswordServiceImpl implements PasswordService {
	@Autowired
	private AdminService adminService;

	@Autowired
	private EmailSenderService emailService;

	@Autowired
	private UserService userService;
	@Autowired
	private PasswordRepository passwordRepository;
	@Autowired
	private AdminRepository adminRepository;

	@Value("${frontend.host}")
	private String frontendHost;

	public void forgotPassword(String email) {
		List<User> users = userService.getByEmail(email);
		users.forEach(user -> {
			if (user.getEmail() != null) {
				Password p = addNew(user);
				String content = frontendHost + "/password-recovery/" + p.getToken();
				emailService.sendEmail(user.getEmail(), "Reset password", content, null);
			}
		});

		List<Admin> admins = adminRepository.findByEmail(email);
		admins.forEach(admin -> {
			if (admin.getEmail() != null) {
				Password p = addNew(admin);
				String content = frontendHost + "/password-recovery/" + p.getToken();
				emailService.sendEmail(admin.getEmail(), "Reset password", content, null);
			}
		});
	}

	private Password addNew(User user) {
		Password p = new Password();
		p.setUser(user);
		p.setExpired(new DateTime().plusDays(30).toDate());
		p.setToken(UUID.randomUUID().toString());
		passwordRepository.save(p);
		return p;
	}

	private Password addNew(Admin admin) {
		Password p = new Password();
		p.setAdmin(admin);
		p.setExpired(new DateTime().plusDays(30).toDate());
		p.setToken(UUID.randomUUID().toString());
		passwordRepository.save(p);
		return p;
	}
}
