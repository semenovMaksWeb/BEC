package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
 public enum CommandTypeEnum {
    postgresql("postgresql"),
    returns("return"),
    validate("validate"),
    block("block"),
    convert("convert");

    private final String title;
}
