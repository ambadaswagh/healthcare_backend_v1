package com.healthcare.service;
import com.twilio.rest.api.v2010.account.Message;

public interface SmsSenderService {

	Message sendSms(String toMobilePhone, String smsBody);
	
}
