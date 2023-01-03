package com.example.bec.model.command.sql;

import com.example.bec.model.command.LinkDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandSqlModel extends LinkDate<List<SqlParamsModel>> {
    private String text;
    private String convert;
}
