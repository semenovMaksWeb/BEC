package com.example.bec.model.command.validateParams;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateParamsRules {
    private String type;
    private String error;
    private Map<String, Object> params;

}
