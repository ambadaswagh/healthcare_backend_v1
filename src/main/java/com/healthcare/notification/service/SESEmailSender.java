package com.healthcare.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class SESEmailSender {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment env;

    private String FROM = "noreply@operr.com";
    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
    static String SMTP_USERNAME = "AKIAIFZIQAYOQ2YAIYPQ";  // Replace with your SMTP username.
    static String SMTP_PASSWORD = "ArknRQxD1YgaSFRHrjYazX7JMrlRxTERdkQx0dhENVlz";  // Replace with your SMTP password.
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    static String HOST = "email-smtp.us-east-1.amazonaws.com";

    // The port you will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
    // STARTTLS to encrypt the connection.
    static final int PORT = 25;

    public boolean isTest=false;

    @Async
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        if (null == toEmail) {
            return;
        }
        try {

            if(!isTest) {
                HOST = env.getProperty("aws.ses.smtp.host");
                SMTP_USERNAME = env.getProperty("aws.ses.smtp.username");
                SMTP_PASSWORD = env.getProperty("aws.ses.smtp.password");
            }
            System.out.println("Start send Email to " + toEmail + ", HOST=" + HOST + ", SMTP_USERNAME=" + SMTP_USERNAME);
            log.info("Start send Email to " + toEmail + ", HOST=" + HOST + ", SMTP_USERNAME=" + SMTP_USERNAME);

            // Create a Properties object to contain connection configuration information.
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtps");
            props.put("mail.smtp.port", PORT);

            // Set properties indicating that we want to use STARTTLS to encrypt the connection.
            // The SMTP session will begin on an unencrypted connection, and then the client
            // will issue a STARTTLS command to upgrade to an encrypted connection.
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            // Create a Session object to represent a mail session with the specified properties.
            Session session = Session.getDefaultInstance(props);
            // Create a message with the specified information.
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(FROM, subject, "UTF-8"));
            msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(subject, "UTF-8");
            msg.setContent(body, "text/html; charset=UTF-8");
            // Create a transport.
            Transport transport = session.getTransport();

            // Send the message.
            try {
                // Connect to Amazon SES using the SMTP username and password you specified above.
                transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

                // Send the email.
                transport.sendMessage(msg, msg.getAllRecipients());
                System.out.println("Email sent!");
                log.info("Amazon SES has sent email to " + toEmail);
            } catch (Exception ex) {
                System.out.println("The email was not sent.");
                System.out.println("Error message: " + ex.getMessage());
                log.error("Error message: " + ex.getMessage() + " Cannot send to email " + toEmail);
            } finally {
                // Close and terminate the connection.
                transport.close();
            }
        }catch (Exception ex){
            log.error("Error message: " + ex.getMessage() + " Cannot send to email " + toEmail);
        }

    }
}
