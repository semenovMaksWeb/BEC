package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
 public enum OperatorTypeEnum {
    equals("=="),
    more(">"),
    less("<");

    private final String title;
}
