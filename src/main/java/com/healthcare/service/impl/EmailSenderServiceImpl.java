package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.ListVerifiedEmailAddressesResult;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;
import com.healthcare.service.EmailSenderService;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

	@Value("${amazon.email.from}")
	private String from;
	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public String sendEmail(String to, String subject, String htmlContent, List<String> ccEmail) {
		if (ccEmail == null) {
			ccEmail = new ArrayList<>();
		}
		PropertiesCredentials credentials = null;
		try {
			credentials = new PropertiesCredentials(
					resourceLoader.getResource("classpath:amazon-ses-credential.properties").getInputStream());
		} catch (Exception ex) {
			return null;
		}
		AmazonSimpleEmailService ses = new AmazonSimpleEmailServiceClient(credentials);
		verifyEmailAddress(ses, to);
		verifyEmailAddress(ses, to);
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(to).withCcAddresses(ccEmail))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlContent)))
						.withSubject(new Content().withCharset("UTF-8").withData(subject)))
				.withSource(from);
		SendEmailResult result = ses.sendEmail(request);
		return result.getMessageId();

	}

	private static void verifyEmailAddress(AmazonSimpleEmailService ses, String address) {
		ListVerifiedEmailAddressesResult verifiedEmails = ses.listVerifiedEmailAddresses();
		if (verifiedEmails.getVerifiedEmailAddresses().contains(address))
			return;

		ses.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(address));
		System.out.println("Please check the email address " + address + " to verify it");
		System.exit(0);
	}

}
