package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StoreFindCommandTecEnum {
    getStatusCode("getStatusCode");
    private final String title;
}
