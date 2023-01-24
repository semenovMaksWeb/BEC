package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandTypeFileOperationEnums {
    createFile("createFile"),
    downloadFileUrl("downloadFileUrl"),
    outputString("outputString"),
    outputStream("outputStream");
    private final String title;
}
