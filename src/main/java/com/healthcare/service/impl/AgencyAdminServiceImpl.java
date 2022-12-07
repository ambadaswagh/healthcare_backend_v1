package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.context.Theme;

import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.model.response.Response;
import com.healthcare.repository.AdminRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.AdminService;
import com.healthcare.util.PasswordUtils;

@Service
@Transactional
public class AgencyAdminServiceImpl extends BasicService<Admin, AdminRepository> implements AdminService {
	private static final String KEY = Admin.class.getSimpleName();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RedisTemplate<String, Admin> adminRedisTemplate;

	@Override
	@Transactional
	public Admin getUser(String username) {
		// Admin admin = (Admin) adminRedisTemplate.opsForHash().get(ADMIN_KEY,
		// id);
		return adminRepository.findByUsername(username);
	}

	@Override
	@Transactional
	public Response logout(String sessionId) {
		// TODO Auto-generated method stub
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
		/*
		 * if (adminRedisTemplate.opsForHash().hasKey(KEY, id)) return (Admin)
		 * adminRedisTemplate.opsForHash().get(KEY, id);
		 */
		return adminRepository.findOneAgencyAdmin(id);
	}

	@Override
	@Transactional
	public List<Admin> findAll() {
		return adminRepository.findAllAgencyAdmin();
	}

	public Page<Admin> findAll(Admin entity, Pageable pageable) {
        pageable = checkPageable(pageable);
        return updateAgency(adminRepository.findAllAgencyAdminPageable(pageable));
	}

	private Page<Admin> updateAgency(Page<Admin> adminPage) {
        Set<Long> adminIds = adminPage.getContent().stream().map(admin -> admin.getId()).collect(Collectors.toSet());
        Map<Long, List<AdminAgencyCompanyOrganization>> agencyMap = adminAgencyCompanyOrganizationService.findByAdminIds(adminIds).stream()
                .collect(Collectors.groupingBy(a -> a.getAdmin().getId()));
        return adminPage.map(admin -> {
            if(agencyMap.containsKey(admin.getId())){
                admin.setAgency(agencyMap.get(admin.getId()).get(0).getAgency());
            }
            return admin;
        });
    }

	@Override
	@Transactional
	public Long disableById(Long id) {
		Admin admin = adminRepository.findOneAgencyAdmin(id);
		if (admin != null) {
			admin.setStatus(EntityStatusEnum.DISABLE.getValue());
			admin = adminRepository.save(admin);
			adminRedisTemplate.opsForHash().put(KEY, admin.getId(), admin);
			return admin.getId();
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
		List<Long> roles = new ArrayList<Long>();
		roles.add(Long.valueOf(5));
		roles.add(Long.valueOf(6));
		pageable = checkPageable(pageable);
		return updateAgency(adminRepository.findAllAdminByAdminList(adminIds, roles, pageable));
	}
}
