package com.example.bec.model.command;

import com.example.bec.model.command.convert.CommandConvertModel;
import com.example.bec.model.command.email.CommandEmailModel;
import com.example.bec.model.command.ifs.IfsModel;
import com.example.bec.model.command.sql.CommandSqlModel;
import com.example.bec.model.command.validate.ValidateParamsModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandModel {
    private String key;
    private String type;
    private CommandSqlModel sql;
    private List<ValidateParamsModel> validate;
    private List<CommandModel> children;
    private CommandConvertModel convert;
    private String link;
    private List<IfsModel> ifs;
    private CommandEmailModel email;
}
