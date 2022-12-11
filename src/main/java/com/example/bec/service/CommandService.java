package com.example.bec.service;

import com.example.bec.configuration.PropertiesCustom;
import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.enums.ConvertTypeEnum;
import com.example.bec.model.command.CommandModel;
import com.example.bec.model.command.convert.ConvertModel;
import com.example.bec.model.command.validate.ValidateParamsModel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Service
public class CommandService {
    private final ConvertService convertService;
    private final CommandService commandService;
    private  final  PostgresqlService postgresqlService;
    private final PropertiesCustom propertiesCustom;

    /* todo удалять и писать в runCommand  */
//    private  Map<String, Object> params;
    private final Map<String, Object> dataset = new HashMap<>();
    /* todo удалять и писать в runCommand */

    public CommandService(@Lazy ConvertService convertService, @Lazy CommandService commandService, @Lazy PostgresqlService postgresqlService, PropertiesCustom propertiesCustom) {
        this.convertService = convertService;
        this.commandService = commandService;
        this.postgresqlService = postgresqlService;
        this.propertiesCustom = propertiesCustom;
    }


    private  List<CommandModel> convertConfig(FileService fileService) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileService.readFile(), new TypeReference<List<CommandModel>>(){});
    }

    /* TODO Optional и везде где может передаваться null */
    private Optional<Object> startCommand(List<CommandModel> config, Map<String, Object> params) throws SQLException, IOException {
        for (CommandModel commandModel : config) {
            /* есть обработка ifs */
            if (commandModel.getIfs() != null){
                IfsService ifsService = new IfsService(commandModel.getIfs(), this.dataset, params);
                if (!ifsService.checkIfs()){
                    continue;
                }
            }
            /* Прогон children */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.block.getTitle()) ) {
                Object res = startCommand(commandModel.getChildren(), params);
                /* Прогон children вызвал return и нужно вернуть выше */
                if (res != null){
                    return Optional.of(res);
                }
            }
            /* вызвать другой файл с конфигом и получить от него ответ */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.config_link.getTitle())){
                Optional<Object> result = this.commandService.runCommand(commandModel.getLink(), params);
                /* исключить ошибки валидации если статус не 200 */
                if (
                    result.isPresent() &&
                    result.get() instanceof ResponseEntity &&
                    !((ResponseEntity<?>) result.get()).getStatusCode().equals(HttpStatus.OK))
                {
                    return result;
                } else {
                    this.dataset.put(commandModel.getKey(),result);
                }
            }
            /*Return команды */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.returns.getTitle()) ) {
                return Optional.of(this.dataset.get(commandModel.getKey()));

            }
            /*Вызов sql postgresql */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.postgresql.getTitle()) ) {
                this.dataset.put(
                    commandModel.getKey(),
                    this.postgresqlService.runSql(
                        commandModel.getSql(),
                        params,
                        this.dataset
                    )
                );
            }
            /*Вызов валидации параметров */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.validate.getTitle()) ) {
                ResponseEntity<Map<String, List<String>>> mapResponseEntity = ValidateParams(commandModel.getValidate(), params);
                if (!mapResponseEntity.getStatusCode().equals(HttpStatus.OK) ){
                    return Optional.of(mapResponseEntity);
                }
            }
            /*Вызов конвертации данных */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.convert.getTitle()) ) {
                convertDataset(commandModel.getConvert().getDataset(), this.dataset);
                convertDataset(commandModel.getConvert().getParams(), params);
            }
        }
        return Optional.empty();
    }
    public Optional<Object> runCommand(String  url, Map<String, Object> params) throws IOException, SQLException {
        /* TODO убрать такое! this проблема сингл тон */
       FileService fileService = new FileService(this.propertiesCustom.getProperties().getProperty("url.config.back") + "\\" + url);
        /* TODO убрать такое! this проблема сингл тон */
        return startCommand(convertConfig(fileService), params);
    }

    /* old */
    private String regularString(JsonNode regular, String text, Map<String, Object> params){
        RegularService regularService  = new RegularService(params);
        String res = text;
        if (regular.isArray()){
            Iterator<JsonNode> itr = regular.elements();
            while (itr.hasNext()){
                res = regularService.startRegular(itr.next().asText(), res);
            }
        }
        return res;
    }

    /* Todo пересмотреть метод ибо нужно прокидывать и куда сохранять и куда смотреть и тд */
    private void convertDataset(List<ConvertModel> listConvertModel, Map<String, Object> dataset) throws IOException {
        if (listConvertModel != null){
            for (ConvertModel convertModel:listConvertModel){
                Object res = null;
                if (convertModel.getType().equals(ConvertTypeEnum.hashPassword.getTitle())){
                    res = this.convertService.hashPassword((String) dataset.get(convertModel.getKey()));
                }
                if (convertModel.getType().equals(ConvertTypeEnum.createToken.getTitle())) {
                    res = null;
//                  res = this.convertService.createToken(convertModel);
                }
                if (convertModel.getType().equals(ConvertTypeEnum.saveValue.getTitle())){
                    res = this.convertService.saveValue(convertModel);
                }
                dataset.put(convertModel.getKey(), res);
            }
        }

    }

    private ResponseEntity<Map<String, List<String>>> ValidateParams(List<ValidateParamsModel> validate, Map<String, Object> params ){
        ValidateService validateService = new ValidateService(params, validate);
        validateService.validateStart();
        if (!validateService.getResult().isEmpty()){
            return new ResponseEntity<>(validateService.getResult(), HttpStatus.BAD_REQUEST);
        };
        return new ResponseEntity<>(validateService.getResult(), HttpStatus.OK);
    }
}