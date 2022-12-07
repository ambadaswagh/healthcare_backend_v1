package com.healthcare.notification.service;


import com.healthcare.model.response.Response;

public interface SESService {
    /**
     * Send simple email
     *
     * @param to
     * @param subject
     * @param body
     */
    Response sendSimpleEmail(String to, String subject, String body);
}