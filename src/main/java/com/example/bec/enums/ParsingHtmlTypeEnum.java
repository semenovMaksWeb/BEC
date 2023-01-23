package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ParsingHtmlTypeEnum {
    connectSite("connectSite"),
    selectElements("selectElements"),
    selectElement("selectElement"),
    selectElementText("selectElementText"),
    selectElementAttr("selectElementAttr");

    private final String title;
}
