package com.example.bec.service;

import com.example.bec.PropertiesCustom;
import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.model.command.Command;
import com.example.bec.model.command.CommandSql;
import com.example.bec.model.command.validateParams.ValidateParams;

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
    private FileService fileService;
    private  Map<String, Object> params;
   private final Map<String, Object> dataset = new HashMap<>();

    private  List<Command> convertConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.fileService.readFile(), new TypeReference<List<Command>>(){});
    }

    public Object runCommand(String  url, Map<String, Object> params) throws IOException, SQLException {
        this.params = params;
        this.fileService = new FileService(PropertiesCustom.getName("url.config.back") + "\\" + url);
        List<Command> config = convertConfig();

        for (Command command : config) {
            /*Return команды */
            if (Objects.equals(command.getType(), CommandTypeEnum.returns.getTitle()) ) {
                return this.dataset.get(command.getKey());
            }
            /*Вызов sql postgresql */
            if (Objects.equals(command.getType(), CommandTypeEnum.postgresql.getTitle()) ) {
                this.dataset.put(command.getKey(), runPostgresqlService(command.getSql()));
            }
            /*Вызов валидации параметров */
            if (Objects.equals(command.getType(), CommandTypeEnum.validate.getTitle()) ) {
               if (ValidateParams(command.getValidate()) != null){
                   return ValidateParams(command.getValidate());
               }
            }
        }
        return null;
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

    private ResponseEntity<Map<String, List<String>>> ValidateParams(List<ValidateParams> validate){
        ValidateService validateService = new ValidateService(this.params, validate);
        validateService.validateStart();
        if (!validateService.getResult().isEmpty()){
            return new ResponseEntity<>(validateService.getResult(), HttpStatus.BAD_REQUEST);
        };
        return null;
    }

    private List<Object> runPostgresqlService(CommandSql commandSql) throws SQLException, IOException {
        PostgresqlService postgresqlService = new PostgresqlService();
        List<Object> res = postgresqlService.runSql(
                commandSql,
                this.params,
                this.dataset
        );
        postgresqlService.closeConn();
        return res;
    }
}