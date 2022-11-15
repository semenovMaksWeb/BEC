package com.example.bec.enums;

 public enum CommandTypeEnum {
    postgresql("postgresql"),
    returns("return");

    private final String title;
    CommandTypeEnum(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}