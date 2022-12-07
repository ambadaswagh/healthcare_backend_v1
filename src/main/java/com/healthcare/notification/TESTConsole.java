//package com.healthcare.notification;
//
//
//import com.healthcare.notification.service.SESEmailSender;
//import com.healthcare.notification.service.SMSSender;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class TESTConsole  {
//
//	public static void main(String[] args) {
//
//
//        SESEmailSender sesEmailSender=new SESEmailSender();
//        sesEmailSender.isTest=true;
//        SMSSender smsSender=new SMSSender();
//        smsSender.isTest=true;
//
//        // Test send Email
//        sesEmailSender.sendSimpleEmail("johnny.lxa@gmail.com","TEST","TEST");
//        // Test send SMS
//        smsSender.sendSimpleSMS("+84933305363","TEST");
//	}
//
//}
