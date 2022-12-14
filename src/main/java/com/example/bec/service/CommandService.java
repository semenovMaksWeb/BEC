package com.example.bec.service;

import com.example.bec.configuration.PropertiesCustom;
import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.enums.ConvertTypeEnum;
import com.example.bec.model.command.CommandModel;
import com.example.bec.model.command.SelectItemModel;
import com.example.bec.model.command.convert.ConvertModel;
import com.example.bec.model.command.validate.ValidateParamsModel;

import com.example.bec.utils.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Service
public class CommandService {
    private final ConvertService convertService;
    private final CommandService commandService;

    private final EmailCustomService emailCustomService;
    private final PostgresqlService postgresqlService;
    private final PropertiesCustom propertiesCustom;

    public CommandService(
            @Lazy ConvertService convertService,
            @Lazy CommandService commandService,
            @Lazy EmailCustomService emailCustomService,
            @Lazy PostgresqlService postgresqlService,
            PropertiesCustom propertiesCustom
    ) {
        this.convertService = convertService;
        this.commandService = commandService;
        this.emailCustomService = emailCustomService;
        this.postgresqlService = postgresqlService;
        this.propertiesCustom = propertiesCustom;
    }


    public List<CommandModel> convertConfig(String url) throws IOException {
        FileUtils fileUtils = new FileUtils(this.propertiesCustom.getProperties().getProperty("url.config.back") + "\\" + url);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileUtils.readFile(), new TypeReference<List<CommandModel>>(){});
    }
    public Optional<Object> startCommand(
            List<CommandModel> config,
            Map<String, Object> params
    ) throws SQLException, MessagingException, IOException {
        return this.startCommand(config, params, new HashMap<>());
    }


    /* TODO Optional ?? ?????????? ?????? ?????????? ???????????????????????? null */
    public Optional<Object> startCommand(
            List<CommandModel> config,
            Map<String, Object> params,
            Map<String, Object> dataset
        ) throws SQLException, IOException, MessagingException {
        for (CommandModel commandModel : config) {
            /* ???????? ?????????????????? ifs */
            if (commandModel.getIfs() != null){
                IfsUtils ifsUtils = new IfsUtils(commandModel.getIfs(), dataset, params);
                if (!ifsUtils.checkIfs()){
                    continue;
                }
            }
            /* ???????????? children */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.block.getTitle()) ) {
                Optional<Object> res = startCommand(commandModel.getChildren(), params, dataset);
                /* ???????????? children ???????????? return ?? ?????????? ?????????????? ???????? */
                if (res.isPresent()){
                    return res;
                }
            }
            /* ?????????????? ???????????? ???????? ?? ???????????????? ?? ???????????????? ???? ???????? ?????????? */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.config_link.getTitle())){
                Optional<Object> result = this.commandService.runCommand(commandModel.getLink(), params);
                /* ?????????????????? ???????????? ?????????????????? ???????? ???????????? ???? 200 */
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
            /* email ?????????????? */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.email.getTitle())) {
                Map<String, Object> result = commandModel.getEmail().generatorResult(dataset, params);
                System.out.println(result);
                this.emailCustomService.sendSimpleEmailTemplate(
                        result.get("from").toString(),
                        result.get("subject").toString(),
                        result.get("template").toString(),
                        (Map<String, Object>) result.get("params")
                );
            }
            /*Return ?????????????? */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.returns.getTitle()) ) {
                return Optional.of(dataset.get(commandModel.getKey()));

            }
            /*?????????? sql postgresql */
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
            /*?????????? ?????????????????? ???????????????????? */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.validate.getTitle()) ) {
                ResponseEntity<Map<String, List<String>>> mapResponseEntity = ValidateParams(commandModel.getValidate(), params);
                if (!mapResponseEntity.getStatusCode().equals(HttpStatus.OK) ){
                    return Optional.of(mapResponseEntity);
                }
            }
            /*?????????? ?????????????????????? ???????????? */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.convert.getTitle()) ) {
                convertDataset(commandModel.getConvert().getDataset(), dataset, params, dataset);
                convertDataset(commandModel.getConvert().getParams(), params, params, dataset);
            }
        }
        return Optional.empty();
    }
    public Optional<Object> runCommand(String  url, Map<String, Object> params) throws IOException, SQLException, MessagingException {
        return startCommand(convertConfig(url), params);
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

    private void convertDataset(List<ConvertModel> listConvertModel, Map<String, Object> link, Map<String, Object> params, Map<String, Object> dataset) throws IOException {
        if (listConvertModel != null){
            MapChildrenDatasetUtils mapChildrenDatasetUtils = new MapChildrenDatasetUtils(dataset, params);
            for (ConvertModel convertModel:listConvertModel){
                Object res = null;
                Map<String, Object> data = mapChildrenDatasetUtils.getObject(convertModel.getParams());

                if (convertModel.getType().equals(ConvertTypeEnum.hashPassword.getTitle())){
                    res = this.convertService.hashPassword(data);
                }

                if (convertModel.getType().equals(ConvertTypeEnum.createToken.getTitle())) {
                    res = this.convertService.createToken(data);
                }

                if (convertModel.getType().equals(ConvertTypeEnum.checkPassword.getTitle())) {
                    res = this.convertService.checkPassword(data);
                }
                if (convertModel.getType().equals(ConvertTypeEnum.constValue.getTitle())){
                    res = data.get("const_name");
                }
                convertModel.updateData(link, res);
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