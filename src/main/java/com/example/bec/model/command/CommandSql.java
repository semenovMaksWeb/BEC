package com.example.bec.model.command;

import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.model.command.sql.SqlParams;

import java.util.List;

public class CommandSql extends Command{
    private final String  text;
    private final List<SqlParams> params;
    public CommandSql(String key, CommandTypeEnum type, String text, List<SqlParams> params) {
        super(key, type);
        this.text = text;
        this.params = params;
    }

    public String getText() {
        return text;
    }

    public List<SqlParams> getParams() {
        return params;
    }
}
