package com.example.bec.model.command.validate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateParamsRulesModel {
    private String type;
    private String error;
    private Map<String, Object> params;

}
