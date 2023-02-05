package com.example.bec.service;

import com.example.bec.configuration.PropertiesConfig;
import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.model.command.CommandModel;
import com.example.bec.model.command.store.StoreCommandModel;
import com.example.bec.utils.FileUtils;
import com.example.bec.utils.IfsUtils;
import com.example.bec.utils.ValidateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CommandService {
    private final PropertiesConfig propertiesConfig;
    private final PostgresqlService postgresqlService;
    private final EmailCustomService emailCustomService;
    private final ConvertService convertService;
    private final ParsingHtmlSiteService parsingHtmlSiteService;
    private final FileService fileService;

    private final ExcelService excelService;
    public CommandService(
            PropertiesConfig propertiesConfig,
            @Lazy PostgresqlService postgresqlService,
            @Lazy EmailCustomService emailCustomService,
            @Lazy ConvertService convertService,
            @Lazy ParsingHtmlSiteService parsingHtmlSiteService,
            @Lazy FileService fileService,
            @Lazy ExcelService excelService
    ){
        this.propertiesConfig = propertiesConfig;
        this.postgresqlService = postgresqlService;
        this.emailCustomService = emailCustomService;
        this.convertService = convertService;
        this.parsingHtmlSiteService = parsingHtmlSiteService;
        this.fileService = fileService;
        this.excelService = excelService;
    }

    private List<CommandModel> getConfigFileName(String url) throws IOException {
        FileUtils fileUtils = new FileUtils(this.propertiesConfig.getProperties().getProperty("url.config.back") + "\\" + url);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(fileUtils.readFile(), new TypeReference<List<CommandModel>>(){});
    }

    /* config и params */
    public Object run(List<CommandModel> config, Map<String, Object> params) throws SQLException, MessagingException, IOException, InvalidFormatException {
        StoreCommandModel storeCommandModel = new StoreCommandModel(propertiesConfig);
        storeCommandModel.updateData(params);
        return this.run(config, storeCommandModel);
    }

    /* url file и store */
    public Object run(String url, StoreCommandModel storeCommandModel) throws IOException, SQLException, MessagingException, InvalidFormatException {
        List<CommandModel> config = this.getConfigFileName(url);
        return this.run(config, storeCommandModel);
    }

    /* url file и params */
    public Object run(String  url, Map<String, Object> params) throws IOException, SQLException, MessagingException, InvalidFormatException {
        List<CommandModel> config = this.getConfigFileName(url);
        StoreCommandModel storeCommandModel = new StoreCommandModel(propertiesConfig);
        storeCommandModel.updateData(params);
        return this.run(config, storeCommandModel);
    }

    /*  config и store */
    public Object run(List<CommandModel> config, StoreCommandModel storeCommandModel) throws SQLException, IOException, MessagingException, InvalidFormatException {
        /* прогон команд */
        for (CommandModel commandModel : config) {
            /* ifs */
            if (commandModel.getIfs() != null) {
                IfsUtils ifsUtils = new IfsUtils(commandModel.getIfs(), storeCommandModel);
                if (!ifsUtils.checkIfs()){
                    continue;
                }
            }
            /* return */
            if (Objects.equals(commandModel.getType(), CommandTypeEnum.returns.getTitle()) ) {
                return storeCommandModel.searchValue(commandModel.getKey());
            }
            /* sql */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.postgresql.getTitle()) ) {
                storeCommandModel.updateValue(
                        commandModel.getKey(),
                        postgresqlService.runSql(commandModel.getSql(), storeCommandModel)
                );
            }
            /* block */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.block.getTitle()) ) {
                Object res = run(commandModel.getChildren(), storeCommandModel);
                if (res != null){
                    return res;
                }
            }
            /* config_link */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.config_link.getTitle())) {
                Object res = run(commandModel.getLink(), storeCommandModel);
                if (res != null){
                    return res;
                }
            }
            /* excel */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.excel.getTitle())) {
                this.excelService.configExcel(storeCommandModel, commandModel.getExcel());
            }
            /* email */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.email.getTitle())) {
                Map<String, Object> result = commandModel.getEmail().generatorResult(storeCommandModel);
                this.emailCustomService.sendSimpleEmailTemplate(result);
            }
            /* parsing_html */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.parsing_html.getTitle())) {
                storeCommandModel.updateValue(
                        commandModel.getKey(),
                        this.parsingHtmlSiteService.parsingConfig(commandModel, storeCommandModel)
                );
            }
            /* validate */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.validate.getTitle()) ) {
                ValidateUtils validateUtils = new ValidateUtils(storeCommandModel);
                storeCommandModel.updateValue(
                        commandModel.getKey(),
                        validateUtils.validateStart(commandModel.getValidate())
                );
            }
            /* convert */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.convert.getTitle()) ) {
                this.convertService.convertConfig(commandModel.getConvert(), storeCommandModel, commandModel.getKey());
            }
            /* foreach */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.foreach.getTitle()) ) {
                List<Object> list = (List<Object>) storeCommandModel.searchValue(commandModel.getForeach().getList());
                for (Object elem : list) {
                    storeCommandModel.updateValue(
                            commandModel.getForeach().getElem(),
                            elem
                    );
                    Object res = run(commandModel.getChildren(), storeCommandModel);
                    if (res != null){
                        return res;
                    }
                }
            }
            /* file */
            else if (Objects.equals(commandModel.getType(), CommandTypeEnum.file.getTitle()) ) {
                this.fileService.fileConfig(commandModel.getFile(), storeCommandModel);
            }
        }
    return null;
    }
}
