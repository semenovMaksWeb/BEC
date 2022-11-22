package com.example.bec.model.command;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandSqlModel{
    private String  text;
    private List<SqlParamsModel> params;
    private List<SqlParamsModel> dataset;
    private  String convert;
}
