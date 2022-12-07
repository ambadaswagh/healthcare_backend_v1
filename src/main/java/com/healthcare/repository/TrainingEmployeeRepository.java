package com.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.TrainingEmployee;

@Repository
public interface TrainingEmployeeRepository extends JpaRepository<TrainingEmployee, Long>, JpaSpecificationExecutor<TrainingEmployee> {

}