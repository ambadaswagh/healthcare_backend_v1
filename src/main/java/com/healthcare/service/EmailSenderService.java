package com.healthcare.service;

import java.util.List;

public interface EmailSenderService {
	public String sendEmail(String to, String subject, String htmlContent, List<String> ccEmail);
}
