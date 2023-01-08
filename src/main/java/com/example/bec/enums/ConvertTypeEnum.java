package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
 public enum ConvertTypeEnum {
    hashPassword("hashPassword"),
    constValue("constValue"),
    checkPassword("checkPassword"),
    createToken("createToken");

    private final String title;
}
