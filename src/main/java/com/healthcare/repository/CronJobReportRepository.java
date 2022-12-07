package com.healthcare.repository;

import com.healthcare.model.entity.Activity;
import com.healthcare.model.entity.CronJobReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobReportRepository extends JpaRepository<CronJobReport, Long> {

}