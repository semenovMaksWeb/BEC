package com.example.bec.controller;

import com.example.bec.configuration.PropertiesCustom;
import com.example.bec.service.EmailCustomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
public class EmailController {
    private  final EmailCustomService emailCustomService;
    private final PropertiesCustom propertiesCustom;

    public EmailController(EmailCustomService emailCustomService, PropertiesCustom propertiesCustom) {
        this.emailCustomService = emailCustomService;
        this.propertiesCustom = propertiesCustom;
    }

    @RequestMapping(
            value = "/email_test",
            method = RequestMethod.POST
    )
    public boolean emailTest() throws MessagingException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("name_user", "Семенов Максим");
        this.emailCustomService.sendSimpleEmailTemplate(
                "msZatoshka@yandex.ru",
                "тестовое письмо",
                "\\confirmed",
                params
        );
        return true;
    }
}
