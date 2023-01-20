package com.example.bec.v2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StoreCommandTypeEnums {
    dataset("dataset"),
    value("value"),
    params("params");

    private final String title;
}
