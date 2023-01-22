package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StoreCommandTypeEnums {
    dataset("dataset"),
    value("value");
    private final String title;
}
