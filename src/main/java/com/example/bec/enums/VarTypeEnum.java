package com.example.bec.enums;

 public enum VarTypeEnum {
    string("string"),
    integer("integer");

    private final String title;
    VarTypeEnum(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
