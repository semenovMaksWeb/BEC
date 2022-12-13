package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
 public enum OperatorTypeEnum {
    equals("=="),
    more(">"),
    less("<"),
    and("&"),
    or("|");

    private final String title;
}
