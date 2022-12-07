package com.healthcare.notification;

import com.healthcare.model.response.Response;
import com.healthcare.notification.service.SESService;
import com.healthcare.notification.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    SESService sesService;

    @Autowired
    SMSService smsService;

    /**
     *
     * @param to
     * @param subject
     * @param body
     * @return
     */
    public Response sendEmail(String to, String subject, String body){
        return sesService.sendSimpleEmail(to, subject, body);
    }


    /**
     *
     * @param phoneNumber
     * @param message
     * @return
     */
    public Response sendSMS(String phoneNumber, String message){
        return smsService.sendSimpleSMS(phoneNumber,message);
    }
}
