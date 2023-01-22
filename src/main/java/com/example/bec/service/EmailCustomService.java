package com.example.bec.service;

import com.example.bec.configuration.TemplateEngineConfig;
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
    public void sendSimpleEmailTemplate(Map<String, Object> params) throws MessagingException {
        this.sendSimpleEmailTemplate(
                params.get("from").toString(),
                params.get("subject").toString(),
                params.get("template").toString(),
                (Map<String, Object>) params.get("params")
        );
    }

    public void sendSimpleEmailTemplate(
            String toAddress,
            String subject,
            String template,
            Map<String, Object> params
    ) throws MessagingException {
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
