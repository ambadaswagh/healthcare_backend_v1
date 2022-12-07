package com.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.AgencyType;

@Repository
public interface AgencyTypeRepository extends JpaRepository<AgencyType, Long>, JpaSpecificationExecutor<AgencyType> {

}