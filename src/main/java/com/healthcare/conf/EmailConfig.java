package com.healthcare.conf;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

	@Value("${view.process:true}")
	private boolean isProcessView;

    @Value("${mailSender.host}")
    private String host;

    @Value("${mailSender.port}")
    private int port;

    @Value("${mailSender.username}")
    private String username;

    @Value("${mailSender.password}")
    private String password;
    
	@Bean(name = "mailSender")
    public JavaMailSenderImpl sendMailConfig() {
        JavaMailSenderImpl bean = new JavaMailSenderImpl();
        bean.setHost(host);
        bean.setPort(port);
        bean.setUsername(username);
        bean.setPassword(password);
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.debug", "true");
        bean.setJavaMailProperties(javaMailProperties);
        return bean;
    }
}
