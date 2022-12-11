package com.example.bec.model.command.validate;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateParamsModel {
    private String key;
    private List<ValidateParamsRulesModel> riles;
}
