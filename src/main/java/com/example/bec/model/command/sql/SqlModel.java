package com.example.bec.model.command.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SqlModel {
    private String text;
    private String convert;
    private List<SqlParamsModel> params;
}
