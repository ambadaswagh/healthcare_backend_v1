package com.healthcare.notification.service;

import com.healthcare.model.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service for sending email
 */
@Service
public class SESServiceImpl implements SESService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SESEmailSender sesEmailSender;


    @Override
    public Response sendSimpleEmail(String toEmail, String subject, String body) {
        if (toEmail == null){
            return new Response(Response.ResultCode.ERROR, null, "Invalid to email.");
        }else{
            // Send email async for quickly response
            sesEmailSender.sendSimpleEmail(toEmail, subject, body);
            // Tracking send email ses history
            return new Response(Response.ResultCode.SUCCESS, "", "OK");
        }
    }

}
