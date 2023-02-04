package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandExcelType {
    createSheet("createSheet"),
    getSheet("getSheet"),
    saveExcelResponse("saveExcelResponse"),
    saveExcelFile("saveExcelFile"),
    createCell("createCell"),
    createRowArrayCell("createRowArrayCell"),
    generatorDataExcel("generatorDataExcel");
    private final String title;
}
