package com.example.bec.controller;

import com.example.bec.service.EmailCustomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class EmailController {
    private  final EmailCustomService emailCustomService;

    public EmailController(EmailCustomService emailCustomService) {
        this.emailCustomService = emailCustomService;
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.POST
    )
    public void emailTest(){
        this.emailCustomService.sendSimpleEmail("msZatoshka@yandex.ru", "тестовое письмо", "тест письмо!!");
    }
}
