package com.example.bec.service;

import com.example.bec.configuration.PropertiesCustom;
import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.enums.ConvertTypeEnum;
import com.example.bec.model.command.CommandModel;
import com.example.bec.model.command.SelectItemModel;
import com.example.bec.model.command.convert.ConvertModel;
import com.example.bec.model.command.validate.ValidateParamsModel;

import com.example.bec.utils.FileUtils;
import com.example.bec.utils.IfsUtils;
import com.example.bec.utils.RegularUtils;
import com.example.bec.utils.ValidateUtils;
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

    public CommandService(@Lazy ConvertService convertService, @Lazy CommandService commandService, @Lazy PostgresqlService postgresqlService, PropertiesCustom propertiesCustom) {
        this.convertService = convertService;
        this.commandService = commandService;
        this.postgresqlService = postgresqlService;
        this.propertiesCustom = propertiesCustom;
    }


    private  List<CommandModel> convertConfig(FileUtils fileUtils) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileUtils.readFile(), new TypeReference<List<CommandModel>>(){});
    }

    /* TODO Optional и везде где может передаваться null */
    private Optional<Object> startCommand(List<CommandModel> config, Map<String, Object> params) throws SQLException, IOException {
        Map<String, Object> dataset = new HashMap<>();
        for (CommandModel commandModel : config) {
            /* есть обработка ifs */
            if (commandModel.getIfs() != null){
                IfsUtils ifsUtils = new IfsUtils(commandModel.getIfs(), dataset, params);
                if (!ifsUtils.checkIfs()){
                    continue;
                }
            }
            /* Прогон children */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.block.getTitle()) ) {
                Optional<Object> res = startCommand(commandModel.getChildren(), params);
                /* Прогон children вызвал return и нужно вернуть выше */
                if (res.isPresent()){
                    return res;
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
                    result.ifPresent(o -> dataset.put(commandModel.getKey(), o));
                }
            }
            /*Return команды */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.returns.getTitle()) ) {
                return Optional.of(dataset.get(commandModel.getKey()));

            }
            /*Вызов sql postgresql */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.postgresql.getTitle()) ) {
                dataset.put(
                    commandModel.getKey(),
                    this.postgresqlService.runSql(
                        commandModel.getSql(),
                        params,
                        dataset
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
                convertDataset(commandModel.getConvert().getDataset(), dataset, params, dataset);
                convertDataset(commandModel.getConvert().getParams(), params, params, dataset);
            }
        }
        return Optional.empty();
    }
    public Optional<Object> runCommand(String  url, Map<String, Object> params) throws IOException, SQLException {
        FileUtils fileUtils = new FileUtils(this.propertiesCustom.getProperties().getProperty("url.config.back") + "\\" + url);
        return startCommand(convertConfig(fileUtils), params);
    }

    /* old */
    private String regularString(JsonNode regular, String text, Map<String, Object> params){
        RegularUtils regularUtils  = new RegularUtils(params);
        String res = text;
        if (regular.isArray()){
            Iterator<JsonNode> itr = regular.elements();
            while (itr.hasNext()){
                res = regularUtils.startRegular(itr.next().asText(), res);
            }
        }
        return res;
    }

    private void convertDataset(List<ConvertModel> listConvertModel, Map<String, Object> result, Map<String, Object> params, Map<String, Object> dataset) throws IOException {
        if (listConvertModel != null){
            for (ConvertModel convertModel:listConvertModel){
                Object res = null;
                if (convertModel.getType().equals(ConvertTypeEnum.hashPassword.getTitle())){
                    res = this.convertService.hashPassword((String) result.get(convertModel.getKey()));
                }
                if (convertModel.getType().equals(ConvertTypeEnum.createToken.getTitle())) {
                    res = this.convertService.createToken(params, dataset, convertModel);
                }
                if (convertModel.getType().equals(ConvertTypeEnum.saveValue.getTitle())){
                    res = this.convertService.saveValue(convertModel);
                }
                result.put(convertModel.getKey(), res);
            }
        }

    }

    private ResponseEntity<Map<String, List<String>>> ValidateParams(List<ValidateParamsModel> validate, Map<String, Object> params ){
        ValidateUtils validateUtils = new ValidateUtils(params, validate);
        validateUtils.validateStart();
        if (!validateUtils.getResult().isEmpty()){
            return new ResponseEntity<>(validateUtils.getResult(), HttpStatus.BAD_REQUEST);
        };
        return new ResponseEntity<>(validateUtils.getResult(), HttpStatus.OK);
    }

    public List<SelectItemModel> getFilesName() throws IOException {
        FileUtils fileUtils = new FileUtils(this.propertiesCustom.getProperties().getProperty("url.config.back"));
        return fileUtils.catalogFileNames();
    }
}