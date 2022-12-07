package com.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.model.entity.InsuranceBrokerCompany;

public interface InsuranceBrokerCompanyRepository extends JpaRepository<InsuranceBrokerCompany, Long> {

}
