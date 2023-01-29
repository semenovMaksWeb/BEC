package com.example.bec.model.command;

import com.example.bec.model.command.file.CommandFileModel;
import com.example.bec.model.command.ifs.IfsModel;

import java.util.List;

import com.example.bec.model.command.sql.SqlModel;
import com.example.bec.model.command.validate.ValidateParamsModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommandModel {
    private List<String> key;
    private List<IfsModel> ifs;
    private String type;
    private List<CommandModel> children; // type = block
    private String link; // ссылка на другой файл конфиг type = config_link
    private SqlModel sql; // type = postgresql
    private CommandEmailModel email; // type = email отправка писем
    private ParsingHtmlSiteModel parsingSite; // type = parsing_html функционал парсинга сайта или html
    private List<ValidateParamsModel> validate; // type = validate
    private CommandConvertModel convert; // type = convert
    private ForeachModel foreach;
    private CommandFileModel file;
    private CommandFileModel excel;
}
