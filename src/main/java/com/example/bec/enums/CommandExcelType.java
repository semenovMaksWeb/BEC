package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandExcelType {
    createSheet("createSheet"),
    getSheet("getSheet"),
    setExcel("setExcel"),
    createCell("createCell"),
    generatorDataExcel("generatorDataExcel");
    private final String title;
}
