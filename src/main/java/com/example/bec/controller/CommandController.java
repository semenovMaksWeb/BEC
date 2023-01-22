package com.example.bec.controller;

import com.example.bec.service.CommandService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("command")
public class CommandController {
    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.POST
    )
    public Object commandRun(
            @ApiParam(required = true, value = "name файла")
            @RequestParam String name,
            @ApiParam(required = true, value = "параметры для команды")
            @RequestBody Map<String, Object> params,
            @RequestHeader(name="Authorization") String token

    ) throws IOException, SQLException, MessagingException {
        params.put("token", token);
        return this.commandService.run(name, params);
    }
}
