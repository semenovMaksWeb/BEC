package com.example.bec.service;

import com.example.bec.PropertiesCustom;
import com.example.bec.enums.CommandTypeEnum;
import com.example.bec.model.command.Command;
import com.example.bec.model.command.CommandSql;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Service
public class CommandService {
    FileService fileService;
    Map<String, Object> params;

    private  List<Command> convertConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.fileService.readFile(), new TypeReference<List<Command>>(){});
    }

    public Object runCommand(String  url, Map<String, Object> params) throws IOException, SQLException {
        this.params = params;
        this.fileService = new FileService(PropertiesCustom.getName("url.config.back") + "\\" + url);
        Map<String, Object> dataset = new HashMap<>();
        List<Command> config = convertConfig();

        for (Command command : config) {
            /*Return команды */
            if (Objects.equals(command.getType(), CommandTypeEnum.returns.getTitle()) ) {
                return  dataset.get(command.getKey());
            }
            /*Вызов sql postgresql */
            if (Objects.equals(command.getType(), CommandTypeEnum.postgresql.getTitle()) ) {
                 dataset.put(command.getKey(), runPostgresqlService(command.getSql()));
            }
        }
        return 1;
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

    private List<Object> runPostgresqlService(CommandSql commandSql) throws SQLException, IOException {
        PostgresqlService postgresqlService = new PostgresqlService();
        List<Object> res = postgresqlService.runSql(
                commandSql.getText(),
                commandSql.getParams(),
                this.params
        );
        postgresqlService.closeConn();
        return res;
    }
}