package com.example.bec.controller;

import com.example.bec.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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
            @RequestParam String hash,
            @ApiParam(required = true, value = "id пользователя")
            @RequestParam String id
    ) throws SQLException, MessagingException, IOException {
        return userService.confirmedUser(hash, id).orElseGet(Optional::empty);
    }
}
