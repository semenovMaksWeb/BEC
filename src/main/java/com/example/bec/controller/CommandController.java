package com.example.bec.controller;

import com.example.bec.service.CommandService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("command")
public class CommandController {
    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService =  commandService;
    }
    @RequestMapping(
            value = "",
            method = RequestMethod.GET
    )
    public Object commandRun(
            @ApiParam(required = true, value = "id скрина")
            @RequestParam String url
    ) throws SQLException, IOException {
        return this.commandService.runCommand(url);
    }
}
