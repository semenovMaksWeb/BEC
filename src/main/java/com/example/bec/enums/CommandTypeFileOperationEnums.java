package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandTypeFileOperationEnums {
    createFile("createFile"),
    downloadFileUrl("downloadFileUrl"),
    outputString("outputString"),
    loaderString("loaderString"),
    fileNameCatalog("fileNameCatalog"),
    catalogFileNamesString("catalogFileNamesString"),
    outputStream("outputStream"),
    outputJson("outputJson");
    private final String title;
}
