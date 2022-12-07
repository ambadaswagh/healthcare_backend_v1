package com.healthcare.util;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;

public class BeanUtils {

	private BeanUtils(){
	}
	
	
	/**
	 * Check the property in the class
	 * @param target : Class
	 * @param propertyName : attribute
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static Boolean objectHasProperty(Class target, String propertyName){
		if(StringUtils.isBlank(propertyName) ||  target == null)
			return false;
		
		boolean isPropertyComposed = propertyName.contains(".");
		String property = propertyName;
		if(isPropertyComposed)
			property = propertyName.split("\\.")[0];
		
	    for(Field field : target.getDeclaredFields()){
	        if(field.getName().equalsIgnoreCase(property)){
	        	if(isPropertyComposed)
	        		return objectHasProperty(field.getType(), propertyName.substring(property.length()+1));
	        	else
	        		return true;
	        }
	    }
	    return false;
	}
}
