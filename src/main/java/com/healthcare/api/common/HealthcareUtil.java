package com.healthcare.api.common;

import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.dto.TimeFilterDTO;
import com.healthcare.dto.VisitDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Visit;
import org.apache.commons.lang.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.healthcare.api.common.HealthcareConstants.*;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

public class HealthcareUtil {

	public static boolean isNull(Long value){
		return value == null || value == 0 ? true : false;
	}
	
	public static boolean isEmpty(String value){
		return value == null || value == "" ? true : false;
	}
	
	public static boolean isEmpty(Object value){
		return value == null || value == "" ? true : false;
	}

	public static Date getDatePart(Date date){
		return DateUtils.truncate(date, java.util.Calendar.DAY_OF_MONTH);
	}

	public static void SET_DATE_RANGE_IF_NOT_PROVIDED(TimeFilterDTO request) {

		if(request.getStartDate()!=null){
			request.setStartDate(getDatePart(request.getStartDate()));
		}
		if(request.getEndDate()!=null){
			request.setEndDate(getDatePart(request.getEndDate()));
		}
		if(request.getStartDate() == null && request.getEndDate() == null){
			request.setEndDate(getDatePart(Calendar.getInstance().getTime()));

			Calendar today = Calendar.getInstance();
			today.add(Calendar.MONTH, -1);

			request.setStartDate(HealthcareUtil.getDatePart(today.getTime()));
		} else if(request.getStartDate() == null){
			request.setStartDate(getDatePart(org.apache.commons.lang.time.DateUtils.addMonths(request.getEndDate(), -1)));
		} else if(request.getEndDate() == null){
			request.setEndDate(getDatePart(org.apache.commons.lang.time.DateUtils.addMonths(request.getStartDate(), 1)));
		}
	}


	public static List<VisitDTO> convertListToDTO(List<Visit> inputList){
	    List<VisitDTO> returnList = new ArrayList <VisitDTO>();

	    if(inputList == null){
            return returnList;
        }

	    for(Visit visit : inputList ){
	        VisitDTO v = new VisitDTO(visit);
            returnList.add(v);
        }

        return  returnList;
    }


    public static long getAgencyIdOfLoggedInAdmin(HttpServletRequest req){
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		/*if(admin.getRole().getAgency() == null){
			throw new UserException("Agency is not configured for this role.");
		}

		if(admin == null || RoleUtil.isAgencyAdmin(admin)){
			throw new UserException("This action requires agency admin privileges.");
		}*/

		return admin.getId();
	}
    
    
    public static long getCompanyIdOfLoggedInAdmin(HttpServletRequest req){
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		
		/*if(admin.getRole().getCompany() == null){
			throw new UserException("Company is not configured for this role.");
		}

		if(admin == null || RoleUtil.isCompanyAdmin(admin)){
			throw new UserException("This action requires company admin privileges.");
		}*/

		return admin.getId();
	}

    
	public static int getCurrentYear(){
    	Calendar c = Calendar.getInstance();
    	return c.get(Calendar.YEAR);
	}
}
