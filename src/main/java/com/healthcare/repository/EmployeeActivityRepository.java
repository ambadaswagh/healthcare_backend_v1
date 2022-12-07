package com.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.EmployeeActivity;

@Repository
public interface EmployeeActivityRepository extends JpaRepository<EmployeeActivity, Long>, JpaSpecificationExecutor<EmployeeActivity> {

}