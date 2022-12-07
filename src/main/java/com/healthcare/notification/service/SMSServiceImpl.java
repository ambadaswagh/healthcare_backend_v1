package com.healthcare.notification.service;

import com.healthcare.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for sending email
 */
@Service
@Slf4j
public class SMSServiceImpl implements SMSService {
    @Autowired
    SMSSender smsSender;


    @Override
    public Response sendSimpleSMS(String phoneNumber, String message) {
        if (phoneNumber == null){
            return new Response(Response.ResultCode.ERROR, null, "Invalid phone number.");
        }else{
            // Send email async for quickly response
            smsSender.sendSimpleSMS(phoneNumber, message);
            // Tracking send email ses history
            return new Response(Response.ResultCode.SUCCESS, "", "OK");
        }
    }
}
