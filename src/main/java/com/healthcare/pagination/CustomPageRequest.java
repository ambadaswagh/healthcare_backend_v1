package com.healthcare.pagination;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.healthcare.util.EnumUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomPageRequest extends PageRequest {

	private static final long serialVersionUID = 8007139341005061681L;

	/**
	 * Default page
	 */
	private static int FIRST_PAGE = 0;
	/**
	 * Default size
	 */
	private static int DEFAULT_SIZE = Integer.MAX_VALUE;

	/**
	 * order direction
	 */
	private String orderIn = Direction.ASC.name();

	/**
	 * order by
	 */
	private String orderBy;
	/**
	 * Ignore case sensitive
	 */
	private boolean ignoreCase = false;

	/**
	 * Constructor
	 */
	public CustomPageRequest() {
		super(FIRST_PAGE, DEFAULT_SIZE);
	}

	/**
	 * Get PageRequest
	 * 
	 * @return Pageable
	 */
	public Pageable getPageRequest() {
		Direction direction = Direction.ASC;
		Sort sort;
		if (StringUtils.isBlank(orderBy)) {
			direction = Direction.DESC;
			sort = new Sort(direction, "createdAt", "updatedAt"); 
		}
		else
		{
			if (StringUtils.isNotBlank(orderIn) && EnumUtils.isInEnum(orderIn.toUpperCase(), Direction.class))
				direction = Direction.valueOf(orderIn.toUpperCase());
			
			Sort.Order customOrder = new Sort.Order(direction, orderBy);
			if (ignoreCase)
				customOrder = customOrder.ignoreCase();
	
			sort = new Sort(customOrder);
		}
		return new PageRequest(getPageNumber(), getPageSize(), sort);
	}

}
