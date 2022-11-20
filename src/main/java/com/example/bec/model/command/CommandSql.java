package com.example.bec.model.command;

import com.example.bec.model.command.sql.SqlParams;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandSql{
    private String  text;
    private List<SqlParams> params;
    private List<SqlParams> dataset;
}
