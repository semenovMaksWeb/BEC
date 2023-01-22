package com.example.bec.model.command.validate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateParamsModel {
    private String key;
    private List<ValidateParamsRulesModel> riles;
}
