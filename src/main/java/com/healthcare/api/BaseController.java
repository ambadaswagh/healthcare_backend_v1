package com.healthcare.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract controller with common methods
 */
public abstract class BaseController {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	/**
	 * Parse entity Id
	 *
	 * @param id
	 *            Id to parse
	 *
	 * @return parsed Id, or null if non-numeric value
	 */
	protected Long parseId(String id) {
		Long result = null;

		try {
			result = Long.valueOf(id);
		} catch (NumberFormatException e) {
			LOGGER.error("Entity Id must be numeric: '{}'", id);
		}

		return result;
	}
}
