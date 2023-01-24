package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StoreCommandTypeEnums {
    dataset("dataset"),
    properties("properties"),
    value("value");
    private final String title;
}
