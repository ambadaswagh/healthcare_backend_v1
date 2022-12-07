package com.healthcare.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import com.google.gson.Gson;

/**
 * JSON-converter
 */
public class UserTypeJsonConverter {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL)
			.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
			.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
			.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
			.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);

	static {
		OBJECT_MAPPER.setConfig(
				OBJECT_MAPPER.getSerializationConfig().without(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS));
	}

	private UserTypeJsonConverter() {
	}

	/**
	 * Convert bean to json string
	 *
	 * @param object
	 *            object to convert
	 *
	 * @return json string
	 */
	public static String toJsonString(@Nullable Object object) {
		String result = "{}";

		try {
			if (null != object) {
				result = OBJECT_MAPPER.writeValueAsString(object);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	/**
	 * Convert MultiValueMap to json string
	 *
	 * @param object
	 *            object to convert
	 *
	 * @return json string
	 */
	public static String toJsonString(Map<String, List<String>> parametres) {
		final Map<String, Object> map = new HashMap<>();
		for (String attribute : parametres.keySet()) {
			List<String> values = parametres.get(attribute);
			if (values != null && !values.isEmpty()) {
				if (values.size() == 1)
					map.put(attribute, values.get(0));
				else
					map.put(attribute, values);
			}
		}

		return new Gson().toJson(map);
	}


	/**
	 * Create bean from json string
	 *
	 * @param string
	 *            json string
	 *
	 * @return converted object
	 */
	public static Object fromJsonString(@Nullable String string, Class targetClass) {
		Object result = null;

		try {
			if (!Strings.isNullOrEmpty(string)) {
				OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				result = OBJECT_MAPPER.readValue(string, targetClass);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	/**
	 * Create bean from creteria string
	 *
	 * @param string
	 *            json string
	 *
	 * @return converted object
	 */
	public static Object fromGetCreteria(@Nullable String string, Class targetClass) {
		Object result = null;

		try {
			if (!Strings.isNullOrEmpty(string)) {
				result = OBJECT_MAPPER.readValue(string, targetClass);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return result;
	}
}
