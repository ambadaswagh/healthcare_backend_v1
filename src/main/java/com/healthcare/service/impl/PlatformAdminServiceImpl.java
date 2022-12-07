package com.healthcare.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.context.Theme;

import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.dto.AdminDTO;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.model.enums.RoleLevelEnum;
import com.healthcare.model.response.Response;
import com.healthcare.repository.AdminRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.AdminService;
import com.healthcare.util.PasswordUtils;

@Service
@Transactional
public class PlatformAdminServiceImpl extends BasicService<Admin, AdminRepository> implements AdminService {
	private static final String KEY = Admin.class.getSimpleName();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AdminRepository adminRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RedisTemplate<String, Admin> adminRedisTemplate;

	@Override
	@Transactional
	public Admin getUser(String username) {
		return adminRepository.findByUsername(username);
	}

	@Override
	@Transactional
	public Response logout(String sessionId) {
		return null;
	}

	@Override
	@Transactional
	public Admin save(Admin admin) {
		admin = adminRepository.save(admin);
		adminRedisTemplate.opsForHash().put(KEY, admin.getId(), admin);
		return admin;
	}

	@Override
	@Transactional
	public Response login(AuthRequest authenticationRequest) {
		Response response = null;
		Admin admin = null;
		try {
			admin = adminRepository.findByUsername(authenticationRequest.getUsername());
			if (admin != null) {
				if (PasswordUtils.checkPassword(authenticationRequest.getPassword(), admin.getPassword())) {
					response = new Response(Response.ResultCode.SUCCESS, admin);
				} else {
					response = new Response(Response.ResultCode.INVALID_PASSWORD, null, "Invalid password");
				}
			} else {
				response = new Response(Response.ResultCode.INVALID_USERNAME, null, "Invalid username");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in AdminServiceImpl, login(), e: " + e.toString());
			response = new Response(Response.ResultCode.ERROR, null, e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public Long deleteById(Long id) {
		adminRepository.delete(id);
		return adminRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	@Transactional
	public Admin findById(Long id) {
		return adminRepository.findOnePlatformAdmin(id);
	}

	@Override
	@Transactional
	public List<Admin> findAll() {
		return adminRepository.findAllPlatformAdmin();
	}
	
	public Page<Admin> findAll(Admin entity, Pageable pageable){
		pageable = checkPageable(pageable);
		return adminRepository.findAllPlatformAdminPageable(pageable);
	}

	@Override
	@Transactional
	public Long disableById(Long id) {
		Admin admin = adminRepository.findOnePlatformAdmin(id);
		if (admin != null) {
			if (admin.getRole() != null && admin.getRole().getLevel() == RoleLevelEnum.SUPER_ADMIN.getValue())
				throw new ApplicationContextException("Super Admin couldn't be disabled");

			if (admin.getRole() != null && admin.getRole().getLevel() == RoleLevelEnum.SUB_SUPER_ADMIN.getValue()) {
				admin.setStatus(EntityStatusEnum.DISABLE.getValue());
				adminRepository.save(admin);
				adminRedisTemplate.opsForHash().put(KEY, admin.getId(), admin);
				return admin.getId();
			}
		}
		return null;
	}

	@Override
	public Admin addNew(Admin admin) {
		admin.setPassword(PasswordUtils.hashPassword(admin.getPassword()));
		admin.setStatus(EntityStatusEnum.ENABLE.getValue());
		return adminRepository.save(admin);
	}

	public boolean isAdminExisted(String username) {
		return adminRepository.findByUsername(username) != null || userRepository.findByUsername(username) != null;
	}
	
	@Override
	public boolean updateTheme(String theme, long userIdPassed) {
		int userId = (int)userIdPassed;
		Theme isThemeExist = adminRepository.selectTheme(userId);
		boolean updated = false;
		if(isThemeExist!=null) {
			adminRepository.deleteTheme(userId);
			adminRepository.insertTheme(theme, userId);
			updated = true;
		}
		else { adminRepository.insertTheme(theme, userId);
		updated = true;
		}
		return updated;
	}

	@Override
	public Page<Admin> findAllAdminByAdminIds(List<Long> adminIds, Pageable pageable) {
		return null;
	}

}
