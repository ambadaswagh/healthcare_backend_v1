package com.healthcare.pagination;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import com.healthcare.util.BeanUtils;
import com.healthcare.util.UserTypeJsonConverter;

import lombok.Data;

/**
 * This class parse all attributes on the URL to apply pagination and Sort
 * result
 * 
 * @author Mostapha
 *
 * @param <E>
 */
@Data
public class MultiValueMapConverter<E> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Pageable to manage pagination
	 */
	private Pageable pageable;

	/**
	 * Object of criteria search
	 */
	private E data;

	/**
	 * Constructor
	 * 
	 * @param multiValueMap
	 *            : MultiValueMap<String, String>
	 * @param target
	 *            : Class
	 */
	public MultiValueMapConverter(MultiValueMap<String, String> multiValueMap, Class<E> target) {
		parseAttributes(multiValueMap, target);
	}

	/**
	 * Parse attributes of GET methode
	 * 
	 * @param multiValueMap
	 *            : map of properties
	 * @param target
	 *            : Class
	 */
	@SuppressWarnings("unchecked")
	private void parseAttributes(MultiValueMap<String, String> multiValueMap, Class<E> target) {
		String jsonObject = StringUtils.EMPTY;
		CustomPageRequest pageRequest;
		if (!multiValueMap.isEmpty()) {
			// Get JsonObject from webRequest
			jsonObject = UserTypeJsonConverter.toJsonString(multiValueMap);
			for (String attribute : multiValueMap.keySet())
				if (BeanUtils.objectHasProperty(target, attribute)) {
					// Get User Object from jsobObject
					this.data = (E) UserTypeJsonConverter.fromJsonString(jsonObject, target);
					break;
				}
			// Get User Pageable from jsobObject
			pageRequest = (CustomPageRequest) UserTypeJsonConverter.fromJsonString(jsonObject, CustomPageRequest.class);
		} else {
			pageRequest = new CustomPageRequest();
		}
		
		if (BeanUtils.objectHasProperty(target, pageRequest.getOrderBy()))
			this.pageable = pageRequest.getPageRequest();
		else if (BeanUtils.objectHasProperty(target, "updatedAt") && BeanUtils.objectHasProperty(target, "createdAt")) {
			pageRequest.setOrderBy(null);
			pageRequest.setOrderIn(null);
			this.pageable = pageRequest.getPageRequest();
		} else
			this.pageable = pageRequest;
	}

}
