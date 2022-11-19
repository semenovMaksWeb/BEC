package com.example.bec.model.command;

import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.model.command.Command;
import com.example.bec.model.command.validateParams.ValidateParams;

import java.util.List;

public class CommandValidateParams extends Command {
    private final List<ValidateParams> params;
    public CommandValidateParams(String key, CommandTypeEnum type, List<ValidateParams> params) {
        super(key, type);

        this.params = params;
    }

    public List<ValidateParams> getParams() {
        return params;
    }
}
