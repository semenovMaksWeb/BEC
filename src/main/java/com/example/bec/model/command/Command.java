package com.example.bec.model.command;

import com.example.bec.enums.CommandTypeEnum;

public class Command {
    private final String key;
    private final CommandTypeEnum type;


    public Command(String key, CommandTypeEnum type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public CommandTypeEnum getType() {
        return type;
    }
}
