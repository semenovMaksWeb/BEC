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
    outputStream("outputStream");
    private final String title;
}
