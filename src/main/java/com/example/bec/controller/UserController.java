package com.example.bec.controller;

import com.example.bec.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(
            value = "/confirmed",
            method = RequestMethod.GET)
    public Object confirmedUser(
            @ApiParam(required = true, value = "hash индификатора пользователя")
            @RequestParam String hash){
        return userService.confirmedUser(hash);
    }
}
