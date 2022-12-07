package com.healthcare.jobs;

import com.healthcare.service.EmployeeService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SickDayCalculationCronJob {

    @Value("${cronjob.enabled}")
    private String conjobEnable;

    static Logger LOGGER = LogManager.getLogger(SeniorAuthorizationExpiringCronJob.class);

    @Autowired
    private EmployeeService employeeService;

    @Scheduled(cron = "${cronjob.employee.sickday.calculation}")
    public void run() {
        if (!Boolean.valueOf(conjobEnable)){
            return;
        }
        LOGGER.info("Scheduled job run 12 AM for sick day calculation");
        employeeService.calculateSickDay();
    }
}
