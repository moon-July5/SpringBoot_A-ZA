package com.moon.aza.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        helper.setFrom("A-ZA"); // 보내는 사람
        helper.setTo(toEmail); // 받는 사람
        helper.setSubject(subject); // 메일 제목
        helper.setText(message, true);  // true는 html 문법 적용

        javaMailSender.send(mimeMessage);
    }
}
