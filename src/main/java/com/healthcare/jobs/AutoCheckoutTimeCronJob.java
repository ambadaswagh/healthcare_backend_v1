package com.healthcare.jobs;

import com.healthcare.service.VisitService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoCheckoutTimeCronJob {
    @Value("${cronjob.enabled}")
    private String conjobEnable;

    static Logger LOGGER = LogManager.getLogger(AutoCheckoutTimeCronJob.class);

    @Autowired
    private VisitService visitService;

    @Scheduled(cron = "${cronjob.auto.checkout.time.schedule}")
    public void run() {
        if (!Boolean.valueOf(conjobEnable)){
            return;
        }
        visitService.doAutoCheckoutTimeByToday();
    }
}
