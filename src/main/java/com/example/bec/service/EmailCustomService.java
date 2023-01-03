package com.example.bec.service;

import com.example.bec.configuration.TemplateEngineConfig;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailCustomService {
    private final JavaMailSender emailSender;
    private final TemplateEngineConfig templateEngineConfig;

    public EmailCustomService(JavaMailSender emailSender, TemplateEngineConfig templateEngineConfig) {
        this.emailSender = emailSender;
        this.templateEngineConfig = templateEngineConfig;
    }

    public void sendSimpleEmail(String toAddress, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        emailSender.send(simpleMailMessage);
    }
    public void sendSimpleEmailTemplate(String toAddress, String subject, String template, Map<String, Object> params) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                true,
                "UTF-8");
        Context context = new Context();
        context.setVariables(params);
        String emailContent = templateEngineConfig.emailTemplateEngine().process(template, context);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(emailContent, true);
        emailSender.send(message);
    }
}
