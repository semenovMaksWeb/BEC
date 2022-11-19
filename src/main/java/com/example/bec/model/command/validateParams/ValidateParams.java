package com.example.bec.model.command.validateParams;

import java.util.List;

public class ValidateParams {
    private final String key;
    private final List<ValidateParamsRules> validate;

    public ValidateParams(String key, List<ValidateParamsRules> validate) {
        this.key = key;
        this.validate = validate;
    }

    public String getKey() {
        return key;
    }

    public List<ValidateParamsRules> getValidate() {
        return validate;
    }
}
