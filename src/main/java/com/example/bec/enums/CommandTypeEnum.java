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
    convert("convert"),
   email("email"),
    config_link("config_link");

    private final String title;
}
