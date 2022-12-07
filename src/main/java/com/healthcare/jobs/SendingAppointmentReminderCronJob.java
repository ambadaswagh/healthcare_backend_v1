package com.healthcare.jobs;

import com.healthcare.service.AppointmentService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SendingAppointmentReminderCronJob {
    @Value("${cronjob.enabled}")
    private String conjobEnable;

    static Logger LOGGER = LogManager.getLogger(SendingAppointmentReminderCronJob.class);

    @Autowired
    private AppointmentService appointmentService;

    @Scheduled(cron = "0 * * * * *")
    public void run() {
        if (!Boolean.valueOf(conjobEnable)){
            return;
        }
        LOGGER.info("Send email to user to  remind appointment");
        appointmentService.doSendingReminder();
    }

}
