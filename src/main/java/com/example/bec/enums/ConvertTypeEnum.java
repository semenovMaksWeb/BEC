package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
 public enum ConvertTypeEnum {
    hashPassword("hashPassword"),
    createToken("createToken");

    private final String title;
}
