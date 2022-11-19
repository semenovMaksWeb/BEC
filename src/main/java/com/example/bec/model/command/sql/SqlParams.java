package com.example.bec.model.command.sql;

import com.example.bec.enums.VarTypeEnum;


public class SqlParams {
    private final VarTypeEnum type;
    private final String key;

    public SqlParams(VarTypeEnum type, String key) {
        this.type = type;
        this.key = key;
    }


    public VarTypeEnum getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
