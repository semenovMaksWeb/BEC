package com.example.bec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public enum RightConstNameEnum {

    namesFileConfigBecGet("names-file-config-bec_get"),
    configCommandGet("config-command_get");

    private final String title;
}
