package com.example.bec.service;

import com.example.bec.configuration.PropertiesCustom;
import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.enums.ConvertTypeEnum;
import com.example.bec.enums.ParsingHtmlTypeEnum;
import com.example.bec.model.command.CommandModel;
import com.example.bec.model.command.SelectItemModel;
import com.example.bec.model.command.convert.ConvertModel;
import com.example.bec.model.command.validate.ValidateParamsModel;

import com.example.bec.utils.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jsoup.nodes.Element;
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

    private final ParsingHtmlSiteService parsingHtmlSiteService;
    private final PropertiesCustom propertiesCustom;

    public CommandService(
            @Lazy ConvertService convertService,
            @Lazy CommandService commandService,
            @Lazy EmailCustomService emailCustomService,
            @Lazy PostgresqlService postgresqlService,
            @Lazy ParsingHtmlSiteService parsingHtmlSiteService,
            PropertiesCustom propertiesCustom
    ) {
        this.convertService = convertService;
        this.commandService = commandService;
        this.emailCustomService = emailCustomService;
        this.postgresqlService = postgresqlService;
        this.parsingHtmlSiteService = parsingHtmlSiteService;
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

    /* TODO Optional и везде где может передаваться null */
    public Optional<Object> startCommand(
            List<CommandModel> config,
            Map<String, Object> params,
            Map<String, Object> dataset
        ) throws SQLException, IOException, MessagingException {
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
                Optional<Object> res = startCommand(commandModel.getChildren(), params, dataset);
                /* Прогон children вызвал return и нужно вернуть выше */
                if (res.isPresent()){
                    return res;
                }
            }
            /* Прогон foreach */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.foreach.getTitle()) ) {
                MapChildrenDatasetUtils mapChildrenDatasetUtils = new MapChildrenDatasetUtils(dataset, params);
                /* массив прогона*/
                List<Object> list = (List<Object>) mapChildrenDatasetUtils.getObjectKey(commandModel.getForeach().getList());
                /* ключ сохранения dataset */
                for (Object el: list){
                    dataset.put(commandModel.getForeach().getElem(), el);
                    Optional<Object> res = startCommand(commandModel.getChildren(), params, dataset);
                    /* Прогон children вызвал return и нужно вернуть выше */
                    if (res.isPresent()){
                        return res;
                    }
                }
            }
            /* прогон parsing html */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.parsing_html.getTitle())){
                dataset.put(commandModel.getKey(), this.parsingHtml(commandModel, params, dataset));
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
            /* email команды */
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
                ResponseEntity<Map<String, List<String>>> mapResponseEntity = validateParams(commandModel.getValidate(), params);
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
                if (convertModel.getType().equals(ConvertTypeEnum.beforeAdd.getTitle())){
                    convertModel.searchData(link,null).toString();
                    res = data.get("value").toString() + convertModel.searchData(link,null).toString();
                }
                convertModel.updateData(link, res);
            }
        }
    }

    private Object parsingHtml(CommandModel commandModel, Map<String, Object> params, Map<String, Object> dataset) throws IOException {
        Object res = null;
        MapChildrenDatasetUtils mapChildrenDatasetUtils = new MapChildrenDatasetUtils(dataset, params);
        if (Objects.equals(commandModel.getParsing().getName(), ParsingHtmlTypeEnum.connectSite.getTitle())){
            res = this.parsingHtmlSiteService.connectSite(
                    mapChildrenDatasetUtils.getObjectKey(commandModel.getParsing().getParams().get("url")).toString()
            );
        }
        if (Objects.equals(commandModel.getParsing().getName(), ParsingHtmlTypeEnum.selectElements.getTitle())){
            res = this.parsingHtmlSiteService.selectElements(
                    mapChildrenDatasetUtils.getObjectKey(commandModel.getParsing().getParams().get("select")).toString(),
                    (Element) mapChildrenDatasetUtils.getObjectKey(commandModel.getParsing().getParams().get("element"))
            );
        }
        if (Objects.equals(commandModel.getParsing().getName(), ParsingHtmlTypeEnum.selectElementAttr.getTitle())){
            res = this.parsingHtmlSiteService.selectElementAttr(
                    mapChildrenDatasetUtils.getObjectKey(commandModel.getParsing().getParams().get("select")).toString(),
                    (Element) mapChildrenDatasetUtils.getObjectKey(commandModel.getParsing().getParams().get("element")),
                    mapChildrenDatasetUtils.getObjectKey(commandModel.getParsing().getParams().get("attr")).toString()
            );
        }
        if (Objects.equals(commandModel.getParsing().getName(), ParsingHtmlTypeEnum.selectElementText.getTitle())){
            res = this.parsingHtmlSiteService.selectElementText(
                    mapChildrenDatasetUtils.getObjectKey(commandModel.getParsing().getParams().get("select")).toString(),
                    (Element) mapChildrenDatasetUtils.getObjectKey(commandModel.getParsing().getParams().get("element"))
            );
        }
        return res;
    }

    private ResponseEntity<Map<String, List<String>>> validateParams(List<ValidateParamsModel> validate, Map<String, Object> params ){
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