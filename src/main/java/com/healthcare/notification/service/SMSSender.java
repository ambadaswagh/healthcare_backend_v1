package com.healthcare.notification.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SMSSender {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment env;

    private String AWS_SMS_ACCESS_KEY = "AKIAIDHKAX4IWZIQ6V7Q";
    private String AWS_SMS_SECRET_KEY = "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";

    public boolean isTest=false;

    @Async
    public void sendSimpleSMS(String phoneNumber, String message){
        if (phoneNumber == null){
            return;
        }

        try {
            // Get access key & secret key from configuration
            if(!isTest) {
                AWS_SMS_ACCESS_KEY = env.getProperty("aws.sns.access_key");
                AWS_SMS_SECRET_KEY = env.getProperty("aws.sns.secret_key");
            }
            System.out.println("AWS Key " + AWS_SMS_ACCESS_KEY);
            System.out.println("AWS Secret " + AWS_SMS_SECRET_KEY);
            // Create Credential
            AWSCredentials awsCredentials =new BasicAWSCredentials(AWS_SMS_ACCESS_KEY, AWS_SMS_SECRET_KEY);
            AmazonSNSClient snsClient = new AmazonSNSClient(awsCredentials);
            // Create list attributes
            Map<String, MessageAttributeValue> smsAttributes =
                    new HashMap<String, MessageAttributeValue>();
            //<set SMS attributes>
            // TODO set

            // Send message
            PublishResult result = snsClient.publish(new PublishRequest()
                    .withMessage(message)
                    .withPhoneNumber(phoneNumber)
                    .withMessageAttributes(smsAttributes));
            System.out.println("Send SMS result: " + result);
            log.info("Send sms sucess " + result);
        }catch (Exception ex){
            log.error("Error message: " + ex.getMessage() + " Cannot send sms to " + phoneNumber);
        }
    }
}
