package com.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.PatientDiagnosis;

@Repository
public interface PatientDaignosisRepository extends JpaRepository<PatientDiagnosis, Long>, JpaSpecificationExecutor<PatientDiagnosis> {
}