package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import com.healthcare.api.auth.AuthTokenFilter;
import com.healthcare.api.auth.AuthTokenUtil;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Role;
import com.healthcare.repository.AdminRepository;
import com.healthcare.repository.MenuRepository;
import com.healthcare.repository.RoleRepository;
import com.healthcare.repository.UserRepository;

@Service
public class CustomUserValidator {

	public static String PLATFORM_ADMIN = "PLATFORM_ADMIN";
	public static String COMPANY_ADMIN = "COMPANY_ADMIN";
	public static String AGENCY_ADMIN = "AGENCY_ADMIN";

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthTokenUtil authTokenUtil;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	MenuRepository menuRepo;

	public void checkUserNameExists(String username) {
		if (adminRepository.findByUsername(username) != null) {
			throw new ApplicationContextException("Username already exists");
		}

		if (userRepository.findByUsername(username) != null) {
			throw new ApplicationContextException("Username already exists");
		}
	}

	public void validateAccess(HttpServletRequest req, long id) {
		Admin admin = adminRepository.findOne(id);
		if (admin == null) {
			throw new ApplicationContextException("Admin does not exists");
		}
		validateAccess(req, admin, true);
	}

	public void validateAccess(HttpServletRequest req, Admin admin) {
		validateAccess(req, admin, false);
	}

	private void validateAccess(HttpServletRequest req, Admin admin, boolean isDelete) {
		String token = req.getHeader(AuthTokenFilter.tokenHeader);

		List<String> menuIdsLoggedInUser = new ArrayList<String>();

		Admin adminObj = getAdmin(token);
		long level = getLevel(token, menuIdsLoggedInUser);

		Role userRole = roleRepository.findOne(adminObj.getRole().getId());
		if (userRole.getLevel() < level) {
			throw new UserException("You do not have permission to create/update/delete admin of higher level");
		}

		if (!isDelete)
			checkMenuAssignBusinessRules(admin, menuIdsLoggedInUser, userRole);
	}

	private Admin getAdmin(String token) {
		String username = authTokenUtil.getUsernameFromToken(token);
		if (username != null) {
			return adminRepository.findByUsername(username);
		}
		return null;
	}

	private long getLevel(String token, List<String> menuIds) {
		Admin admin = getAdmin(token);
		if (admin == null) {
			throw new UserException("You are not authorized to perform this operation");
		}

		menuIds.addAll(admin.getMenulist() == null ? Collections.emptyList() : (admin.getMenulist()));
		return admin.getRole().getLevel();
	}

	private void checkMenuAssignBusinessRules(Admin admin, List<String> menuIdsLoggedInUser, Role newUserRole) {

		List<String> menuIdsToBeGranted = admin.getMenulist() == null ? Collections.emptyList() : (admin.getMenulist());

		if (menuIdsToBeGranted.isEmpty()) {
			return;
		}

		loggedInAdminListIsNotEmpty(menuIdsLoggedInUser);
		removeBooleanValueInMenuList(menuIdsToBeGranted);
		//shouldBeSubSetOfDefaultAccessibleMenus(newUserRole, menuIdsToBeGranted);
		shouldBeSubSetOfLoggedInUserMenuList(menuIdsLoggedInUser, menuIdsToBeGranted);
	}

	private void removeBooleanValueInMenuList(List<String> menuIdsToBeGranted) {
	    menuIdsToBeGranted.removeIf(menuId -> !isInteger(menuId));
    }
	
    public static boolean isInteger(String s) {
        boolean isValidInteger = false;
        try {
            Integer.parseInt(s);
            isValidInteger = true;
        } catch (NumberFormatException ex) {
            // s is not an integer
        }

        return isValidInteger;
    }

    private void shouldBeSubSetOfLoggedInUserMenuList(List<String> menuIdsLoggedInUser,
			List<String> menuIdsToBeGranted) {
		if (!menuIdsLoggedInUser.containsAll(menuIdsToBeGranted)) {
			String msg = getNonAccessibleMenus(menuIdsLoggedInUser, menuIdsToBeGranted);
			throw new UserException(
					"You do not have permission on menus [" + msg + "]. So can not assign to other user");
		}
	}

	/*private void shouldBeSubSetOfDefaultAccessibleMenus(Role newUserRole, List<String> menuIdsToBeGranted) {
		AdminPermission adminPermission = adminPermissionRepository.findDefaultAccessibleMenus(newUserRole.getLevel());

		if (adminPermission.getAccessibleMenu() == null) {
			throw new UserException("No default menu permission exist for this role level");
		}

		if (!adminPermission.getAccessibleMenu().containsAll(menuIdsToBeGranted)) {
			String msg = getNonAccessibleMenus(adminPermission.getAccessibleMenu(), menuIdsToBeGranted);
			throw new UserException(
					"These menus are not in default list of role [" + msg + "]. So can not assign these menu.");
		}
	}*/

	private void loggedInAdminListIsNotEmpty(List<String> menuIdsLoggedInUser) {
		if (menuIdsLoggedInUser.isEmpty()) {
			throw new UserException("You do not have permission on any menu");
		}
	}

	private String getNonAccessibleMenus(List<String> largestScopeMenu, List<String> menuIdsToBeGranted) {
		menuIdsToBeGranted.removeAll(largestScopeMenu);

		List<Long> ids = menuIdsToBeGranted.stream().map(Long::parseLong).collect(Collectors.toList());
		List<String> menuNameList = menuRepo.findMenu(ids);

		String userMsg = menuNameList.stream().map(Object::toString).collect(Collectors.joining(", "));
		return userMsg;
	}

}
