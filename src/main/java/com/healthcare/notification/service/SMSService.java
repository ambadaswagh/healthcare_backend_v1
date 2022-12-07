package com.healthcare.notification.service;


import com.healthcare.model.response.Response;

public interface SMSService {

    Response sendSimpleSMS(String phoneNumber, String message);
}
