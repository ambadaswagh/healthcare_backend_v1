package com.healthcare.jobs;

import com.healthcare.service.RideService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TripCountCalculationCronJob {

    @Value("${cronjob.enabled}")
    private String conjobEnable;

    static Logger LOGGER = LogManager.getLogger(TripCountCalculationCronJob.class);

    @Autowired
    RideService rideService;



    @Scheduled(cron = "${cronjob.tripcount.calculation}")
    public void run() {
        if (!Boolean.valueOf(conjobEnable)){
            return;
        }
        LOGGER.info("Scheduled job run 12 AM for trip count calculation");
        rideService.findByDateMaxTrip();
    }
}
