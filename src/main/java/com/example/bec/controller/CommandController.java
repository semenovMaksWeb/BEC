package com.example.bec.controller;

import com.example.bec.enums.RightConstNameEnum;
import com.example.bec.model.command.CommandModel;
import com.example.bec.service.AuthorizationService;
import com.example.bec.service.CommandService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("command")
public class CommandController {
    private final CommandService commandService;
    private final AuthorizationService authorizationService;

    public CommandController(CommandService commandService, AuthorizationService authorizationService) {
        this.commandService =  commandService;
        this.authorizationService = authorizationService;
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

    ) throws SQLException, IOException, MessagingException {
        params.put("token", token);
        return this.commandService.runCommand(name, params).orElseGet(Optional::empty);
    }

    @RequestMapping(
            value = "/get_names",
            method = RequestMethod.GET
    ) public Object getFilesNames(
            @RequestHeader(name="Authorization") String token
    ) throws SQLException, IOException, MessagingException {
        Optional<Object> result = authorizationService.checkRight(RightConstNameEnum.namesFileConfigBecGet.getTitle(), token);
        if (result.isPresent()){
            return result.get();
        }
        return this.commandService.getFilesName();
    }
    @RequestMapping(
            value = "/get_config",
            method = RequestMethod.GET
    ) public Object getCommandConfig(
            @RequestHeader(name="Authorization") String token,
            @ApiParam(required = true, value = "name файла")
            @RequestParam String name
    ) throws SQLException, IOException, MessagingException {
        Optional<Object> result = authorizationService.checkRight(RightConstNameEnum.configCommandGet.getTitle(), token);
        if (result.isPresent()){
            return result.get();
        }
        return this.commandService.convertConfig(name);
    }

/* todo тест api нужно доделать */
    @RequestMapping(
            value = "/run_command/json",
            method = RequestMethod.POST
    ) public Object startRunCommand(
            @RequestHeader(name="Authorization") String token,
            @ApiParam(required = true, value = "строка конфига")
            @RequestBody String json,
            @ApiParam(required = true, value = "параметры для команды")
            @RequestBody Map<String, Object> params
    ) throws SQLException, IOException, MessagingException {
        Optional<Object> result = authorizationService.checkRight(RightConstNameEnum.configCommandGet.getTitle(), token);
        if (result.isPresent()){
            return result.get();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return  this.commandService.startCommand(
                objectMapper.readValue(json, new TypeReference<List<CommandModel>>(){}),
                params
        );
    }
}
