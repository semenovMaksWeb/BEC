package com.example.bec.controller;

import com.example.bec.enums.RightConstNameEnum;
import com.example.bec.service.CommandService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("command")
public class CommandController {
    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService =  commandService;
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
    ) public Object getFilesNames() throws SQLException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("right_const_name", RightConstNameEnum.namesFileConfigBecGet);
        Optional<Object> result = this.commandService.runCommand("check_right.json", params);
        System.out.println(result.get());
        return this.commandService.getFilesName();
    }

}
