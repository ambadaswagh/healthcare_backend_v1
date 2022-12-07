package com.healthcare.api.common;

import com.healthcare.model.entity.Admin;

public class RoleUtil {

	public static boolean isSuperAdmin(Admin admin){
		return admin.getRole().getLevel() == 1 ||  admin.getRole().getLevel() == 2 ? true : false;
	}
	
	public static boolean isCompanyAdmin(Admin admin){
		return admin.getRole().getLevel() == 3 ||  admin.getRole().getLevel() == 4 ? true : false;
	}
	
	public static boolean isAgencyAdmin(Admin admin){
		return admin.getRole().getLevel() == 5 ||  admin.getRole().getLevel() == 6 ? true : false;
	}
}
