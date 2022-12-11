package com.example.bec.service;

import com.example.bec.PropertiesCustom;
import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.enums.ConvertTypeEnum;
import com.example.bec.model.command.CommandModel;
import com.example.bec.model.command.CommandSqlModel;
import com.example.bec.model.command.ConvertModel;
import com.example.bec.model.command.ValidateParamsModel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Service
public class CommandService {
    /* todo удалять и писать в runCommand  */
    private FileService fileService;
    private  Map<String, Object> params;
    private final Map<String, Object> dataset = new HashMap<>();
    /* todo удалять и писать в runCommand */
    private  List<CommandModel> convertConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.fileService.readFile(), new TypeReference<List<CommandModel>>(){});
    }

    /* TODO Optional и везде где может передаваться null */
    private Object startCommand(List<CommandModel> config) throws SQLException, IOException {
        for (CommandModel commandModel : config) {
            /* есть обработка ifs */
            if (commandModel.getIfs() != null){
                IfsService ifsService = new IfsService(commandModel.getIfs(), this.dataset, this.params);
                if (!ifsService.checkIfs()){
                    continue;
                }
            }
            /* Прогон children */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.block.getTitle()) ) {
                Object res = startCommand(commandModel.getChildren());
                /* Прогон children вызвал return и нужно вернуть выше */
                if (res != null){
                    return  res;
                }
            }
            /* вызвать другой файл с конфигом и получить от него ответ */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.config_link.getTitle())){
                CommandService commandService = new CommandService();
                Object result = commandService.runCommand(commandModel.getLink(), this.params);
                /* исключить ошибки валидации 400 */
                /* todo  ResponseEntity result instanceof */
                if (result != null && result.getClass().getSimpleName().equals("ResponseEntity")){
                    return result;
                }else {
                    this.dataset.put(commandModel.getKey(),result);
                }
            }
            /*Return команды */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.returns.getTitle()) ) {
                return this.dataset.get(commandModel.getKey());

            }
            /*Вызов sql postgresql */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.postgresql.getTitle()) ) {
                this.dataset.put(commandModel.getKey(), runPostgresqlService(commandModel.getSql()));
            }
            /*Вызов валидации параметров */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.validate.getTitle()) ) {
                if (ValidateParams(commandModel.getValidate()) != null){
                    return ValidateParams(commandModel.getValidate());
                }
            }
            /*Вызов конвертации данных */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.convert.getTitle()) ) {
                convertDataset(commandModel.getConvert().getDataset(), this.dataset);
                convertDataset(commandModel.getConvert().getParams(), this.params);
            }
        }
        return null;
    }
    public Object runCommand(String  url, Map<String, Object> params) throws IOException, SQLException {
        /* TODO убрать такое! this проблема сингл тон */
        this.params = params;
        this.fileService = new FileService(PropertiesCustom.getName("url.config.back") + "\\" + url);
        /* TODO убрать такое! this проблема сингл тон */
        List<CommandModel> config = convertConfig();
        return startCommand(config);
    }

    /* old */
    private String regularString(JsonNode regular, String text){
        RegularService regularService  = new RegularService(this.params);
        String res = text;
        if (regular.isArray()){
            Iterator<JsonNode> itr = regular.elements();
            while (itr.hasNext()){
                res = regularService.startRegular(itr.next().asText(), res);
            }
        }
        return res;
    }

    private void convertDataset(List<ConvertModel> listConvertModel, Map<String, Object> dataset) throws IOException {
        ConvertService convertService = new ConvertService();
        if (listConvertModel != null){
            for (ConvertModel convertModel:listConvertModel){
                Object res = null;
                if (convertModel.getType().equals(ConvertTypeEnum.hashPassword.getTitle())){
                    res = convertService.hashPassword((String) dataset.get(convertModel.getKey()));
                }
                if (convertModel.getType().equals(ConvertTypeEnum.createToken.getTitle())) {
                    res = convertService.createToken(this.params);
                }
                if (convertModel.getType().equals(ConvertTypeEnum.saveValue.getTitle())){
                    res = convertService.saveValue(convertModel);
                }
                dataset.put(convertModel.getKey(), res);
            }
        }

    }

    private ResponseEntity<Map<String, List<String>>> ValidateParams(List<ValidateParamsModel> validate){
        ValidateService validateService = new ValidateService(this.params, validate);
        validateService.validateStart();
        if (!validateService.getResult().isEmpty()){
            return new ResponseEntity<>(validateService.getResult(), HttpStatus.BAD_REQUEST);
        };
        return null;
    }

    private Object runPostgresqlService(CommandSqlModel commandSql) throws SQLException, IOException {
        PostgresqlService postgresqlService = new PostgresqlService();
        Object res = postgresqlService.runSql(
                commandSql,
                this.params,
                this.dataset
        );
        postgresqlService.closeConn();
        return res;
    }
}