package com.healthcare.integration.service;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.service.SmsSenderService;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.Message.Status;
import com.twilio.type.PhoneNumber;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SmsSenderServiceTest {
	
	@Autowired
	private SmsSenderService smsSenderService;
	
	/*@MockBean
	private SmsSenderService mockSmsSenderService;*/
	
	@Test
	public void Should_Send_Sms_Message_Successfully(){
		String toMobilePhone = "+33668570564";
		String smsBody = "Health care notification sms body";
		Message message = smsSenderService.sendSms(toMobilePhone, smsBody);
		assertThat(message, notNullValue());
		assertTrue(new PhoneNumber("+13525070474").equals(message.getFrom()));
		assertTrue(Status.QUEUED.equals(message.getStatus()));
	}
	
	@Test(expected=ApiException.class)
	public void Should_Not_Send_Sms_Message_To_Nonmobile_Number() {
		String toMobilePhone = "+336685705647777";
		String smsBody = "Health care notification sms body";
		smsSenderService.sendSms(toMobilePhone, smsBody);
	}
	
}
