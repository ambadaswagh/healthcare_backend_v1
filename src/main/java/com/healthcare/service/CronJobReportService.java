package com.healthcare.service;

import com.healthcare.model.entity.CronJobReport;
import com.healthcare.model.entity.ServicePlan;

import java.util.Date;
import java.util.List;

public interface CronJobReportService extends IService<CronJobReport> {
	public List<CronJobReport> findAll();
}
