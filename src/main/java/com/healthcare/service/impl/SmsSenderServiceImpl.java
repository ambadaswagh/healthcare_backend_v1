package com.healthcare.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.healthcare.service.SmsSenderService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
@Transactional
public class SmsSenderServiceImpl implements SmsSenderService{
	

	@Value("${twilio.sms.account.sid}")
    private String ACCOUNT_SID;
	
	@Value("${twilio.sms.auth.token}")
    private String AUTH_TOKEN;
	
	@Value("${twilio.sms.mobile.phone}")
    private String FROM_MOBILE_PHONE;
	

	@Override
	public Message sendSms(String toMobilePhone, String smsBody){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber(toMobilePhone),  // to
                         new PhoneNumber(FROM_MOBILE_PHONE),  // from
                         smsBody).create();
        return message;
	}

}
