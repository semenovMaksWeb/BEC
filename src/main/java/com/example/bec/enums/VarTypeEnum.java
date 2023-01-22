package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VarTypeEnum {
    string("String"),
    integer("Integer");

    private final String title;
}
