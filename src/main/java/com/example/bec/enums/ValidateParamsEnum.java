package com.example.bec.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ValidateParamsEnum {

    req("req"),
    var("var");
    private final String title;
}
