package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.context.Theme;

import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Menu;
import com.healthcare.model.response.Response;
import com.healthcare.repository.AdminRepository;
import com.healthcare.repository.MenuRepository;
import com.healthcare.service.AdminService;
import com.healthcare.util.PasswordUtils;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
@Primary
public class AdminServiceImpl  extends BasicService<Admin, AdminRepository> implements AdminService {
	private static final String KEY = Admin.class.getSimpleName();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AdminRepository adminRepository;
	
    @Autowired
    MenuRepository menuRepository;

	@Autowired
	private RedisTemplate<String, Admin> adminRedisTemplate;

	@Override @Transactional
	public Admin getUser(String username) {
		// Admin admin = (Admin) adminRedisTemplate.opsForHash().get(ADMIN_KEY,
		// id);
		return adminRepository.findByUsername(username);
	}

	@Override @Transactional
	public Response logout(String sessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override @Transactional
	public Admin save(Admin admin) {
		admin = adminRepository.save(admin);
		adminRedisTemplate.opsForHash().put(KEY, admin.getId(), admin);
		return admin;
	}

	@Override @Transactional
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

	@Override @Transactional
	public Long deleteById(Long id) {
		adminRepository.delete(id);
		return adminRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
    public Admin findById(Long id) {
	    Admin admin = null;
        if (adminRedisTemplate.opsForHash().hasKey(KEY, id)) {
            admin = convertToClass(adminRedisTemplate.opsForHash().get(KEY, id));
        } else {
            admin = adminRepository.findOne(id);
        }
        List<String> resultList = new ArrayList<>();
        List<String> adminMenuList = admin.getMenulist();
        List<Menu> currentMenuList = menuRepository.findAll();
        for (int i = 0; i < currentMenuList.size(); i++) {
            String menuId = String.valueOf(currentMenuList.get(i).getId());
            if (adminMenuList.stream().anyMatch(item -> item.equals(menuId))) {
                resultList.add(menuId);
            } else {
                resultList.add(Boolean.FALSE.toString());
            }
        }
        admin.setMenulist(resultList);
        return admin;
    }

	@Override @Transactional
	public List<Admin> findAll() {
		Map<Object, Object> adminMap = adminRedisTemplate.opsForHash().entries(KEY);
		List<Admin> adminList = Collections.arrayToList(adminMap.values().toArray());
		if (adminMap.isEmpty())
			adminList = adminRepository.findAll();
		return adminList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}

	@Override
	public Admin addNew(Admin admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAdminExisted(String username) {
		// TODO Auto-generated method stub
		return false;
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
