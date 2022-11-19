package com.example.bec.model.command.validateParams;


import java.util.Map;


public class ValidateParamsRules {
    private final String type;
    private final String error;
    private final Map<String, Object> params;

    public ValidateParamsRules(String type, String error, Map<String, Object> params) {
        this.type = type;
        this.error = error;
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getError() {
        return error;
    }

    public String getType() {
        return type;
    }
}
