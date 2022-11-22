package com.example.bec.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IfsModel {
    private IfsParamsModel dataset;
    private IfsParamsModel params;
    private Object value;
    private String operator;


}
