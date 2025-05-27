package com.example.DiscountBackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.example.DiscountBackend.config.EmailConfig;

@Service
public class MailService {

    @Autowired
    private EmailConfig emailConfig; // Inject EmailConfig to create dynamic senders

    @Autowired
    private JavaMailSender defaultMailSender; // Inject the default mail sender

    // ✅ Send email using the default sender
    public void sendCouponEmail(String email, String couponCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Discount Coupon Code");
        message.setText("Here is your coupon code: " + couponCode + "\nEnjoy your discount!");

        defaultMailSender.send(message);
    }

    // ✅ Send email with a specific email account
    public void sendEmailWithCustomAccount(String fromEmail, String password, String toEmail, String subject, String body) {
        JavaMailSender mailSender = emailConfig.javaMailSender();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
