package com.healthcare.jobs;

import com.healthcare.service.CronJobAuthorizationExpiringService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SeniorAuthorizationExpiringCronJob {

    @Value("${cronjob.enabled}")
    private String conjobEnable;

    static Logger LOGGER = LogManager.getLogger(SeniorAuthorizationExpiringCronJob.class);

    @Autowired
    private CronJobAuthorizationExpiringService cronJobAuthorizationExpiringService;

    @Scheduled(cron = "${cronjob.authorizing.expiring}")
    public void run() {
        if (!Boolean.valueOf(conjobEnable)){
            return;
        }
        LOGGER.info("Scheduled job run 12 AM for checking expiring");
        cronJobAuthorizationExpiringService.updateAuthorizationExpiring();
    }

}
