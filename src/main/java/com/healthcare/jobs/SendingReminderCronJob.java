package com.healthcare.jobs;

import com.healthcare.service.NotificationSettingService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SendingReminderCronJob {

    @Value("${cronjob.enabled}")
    private String conjobEnable;

    static Logger LOGGER = LogManager.getLogger(SendingReminderCronJob.class);

    @Autowired
    private NotificationSettingService notificationSettingService;

    @Scheduled(cron = "0 * * * * *")
    public void run() {
        if (!Boolean.valueOf(conjobEnable)){
            return;
        }
        LOGGER.info("Send messsage reminder cronjob");
        notificationSettingService.doSending();
    }
}
