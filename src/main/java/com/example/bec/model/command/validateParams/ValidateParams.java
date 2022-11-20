package com.example.bec.model.command.validateParams;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateParams {
    private String key;
    private List<ValidateParamsRules> validate;
}
