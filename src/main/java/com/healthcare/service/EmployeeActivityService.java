package com.healthcare.service;

import com.healthcare.model.entity.EmployeeActivity;

import java.util.List;

/**
 * Created by jean on 14/06/17.
 */
public interface EmployeeActivityService extends IService<EmployeeActivity>, IFinder<EmployeeActivity> {

    EmployeeActivity save(EmployeeActivity employeeActivity);

    List<EmployeeActivity> findAll();

    EmployeeActivity findById(Long id);

    Long deleteById(Long id);

    Long deleteByEmployeeActivity(EmployeeActivity employeeActivity);

    Long findByEmployeeActivity(EmployeeActivity employeeActivity);

    Long disableById(Long id);
}
