package com.example.bec.controller;

import com.example.bec.enums.RightConstNameEnum;
import com.example.bec.service.AuthorizationService;
import com.example.bec.service.CommandService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
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
            @RequestBody Map<String, Object> params

    ) throws SQLException, IOException {
        return this.commandService.runCommand(name, params).orElseGet(Optional::empty);
    }
    @RequestMapping(
            value = "/get_names",
            method = RequestMethod.GET
    ) public Object getFilesNames(
            @RequestHeader(name="Authorization") String token
    ) throws SQLException, IOException {
        Optional<Object> result = authorizationService.checkRight(RightConstNameEnum.namesFileConfigBecGet.getTitle(), token);
        if (result.isPresent()){
            return result.get();
        }
        return this.commandService.getFilesName();
    }

}
