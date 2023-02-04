package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandExcelType {
    createSheet("createSheet"),
    getSheet("getSheet"),
    setExcel("setExcel"),
    saveExcelResponse("saveExcelResponse"),
    createCell("createCell"),
    createRowArrayCell("createRowArrayCell"),
    generatorDataExcel("generatorDataExcel");
    private final String title;
}
