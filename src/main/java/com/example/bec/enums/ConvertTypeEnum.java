package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConvertTypeEnum {
    hashPassword("hashPassword"),
    constValue("constValue"),
    checkPassword("checkPassword"),
    createToken("createToken"),
    afterAdd("afterAdd"),
    beforeAdd("beforeAdd"),
    addList("addList"),

    createList("createList"),
    substring("substring"),
    createMap("createMap");

    private final String title;
}

