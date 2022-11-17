package com.example.bec.service;

import com.example.bec.PropertiesCustom;
import com.example.bec.enums.CommandTypeEnum;
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
    private List<JsonNode> convertConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(this.fileService.readFile(), typeFactory.constructCollectionType(List.class, JsonNode.class));
    }

    public Object runCommand(String  url, Map<String, Object> params) throws IOException, SQLException {
        this.params = params;
        this.fileService = new FileService(PropertiesCustom.getName("url.config.back") + "\\" + url);
        /* Хранения результатов команд */
        Map<String, Object> dataset = new HashMap<>();
        /* Конфиг команд */
        List<JsonNode> config = convertConfig();
        /* Анализ конфига*/

        for (JsonNode element : config) {
            String key = element.get("key").textValue();
            String type = element.get("type").textValue();
            /* проверка что это return */
            if (Objects.equals(type, CommandTypeEnum.returns.getTitle()) ) {
                return dataset.get(key);
            }

            /* Выполнение какой либо команды */
            else {
                Object result = this.checkTypeCommand(element, type);
                dataset.put(key, result);
            }
        }
        return  null;
    }
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

    private List<Object> runPostgresqlService(JsonNode element) throws SQLException, IOException {
        PostgresqlService postgresqlService = new PostgresqlService();
        List<Object> res = postgresqlService.runSql(
                element.get("sql").get("text").textValue(),
                element.get("sql").get("params"),
                this.params
        );
        postgresqlService.closeConn();
        return res;
    }

    private Object checkTypeCommand(JsonNode element, String type) throws SQLException, IOException {
        if (type.equals(CommandTypeEnum.postgresql.getTitle())) {
            return this.runPostgresqlService(element);
        }
        return null;
    }
}