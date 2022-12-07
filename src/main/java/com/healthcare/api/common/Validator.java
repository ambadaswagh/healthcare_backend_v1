package com.healthcare.api.common;

import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;

import javax.servlet.http.HttpServletRequest;

import static com.healthcare.api.common.HealthcareConstants.*;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.HealthcareUtil.isNull;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

/**
 * Created by Hitesh on 10/3/2017.
 */
public class Validator {

    public static void validateCompanyAndAgency(BasicReportFilterDTO BasicReportFilter) {
        if (isNull(BasicReportFilter.getCompanyId())) {
            throw new UserException("Please select a company.");
        }

        if (isNull(BasicReportFilter.getAgencyId())) {
            throw new UserException("Please select an agency.");
        }
    }

    public static boolean AUTH_ADMIN(HttpServletRequest req, BasicReportFilterDTO basicReportFilterDTO) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if (isEmpty(admin)) {
            throw new UserException(NOT_AUTHORIZED);
        }

        if (isSuperAdmin(admin) )
            return true;

        if(isCompanyAdmin(admin)){
            return true;
        }

        if(isAgencyAdmin(admin)){
           return true;
        }

        return false;
    }

}
