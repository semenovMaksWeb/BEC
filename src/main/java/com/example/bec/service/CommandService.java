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
    RegularService regularService;
    private List<JsonNode> convertConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(this.fileService.readFile(), typeFactory.constructCollectionType(List.class, JsonNode.class));
    }

    public Object runCommand(String  url, Map<String, Object> params) throws IOException, SQLException {
        this.fileService = new FileService(PropertiesCustom.getName("url.config.back") + "\\" + url);
        /* Хранения результатов команд */
        Map<String, Object> dataset = new HashMap<>();
        /* Конфиг команд */
        List<JsonNode> config = convertConfig();
        /* Анализ конфига*/
        this.regularService  = new RegularService(params);
        for (JsonNode element : config) {
            String key = element.get("key").textValue();
            String type = element.get("type").textValue();
            /* проверка что это return */
            if (Objects.equals(type, CommandTypeEnum.returns.getTitle()) ) {
                return dataset.get(key);
            }

            /* Выполнение какой либо команты */
            else {
                Object result = this.checkTypeCommand(element, type);
                dataset.put(key, result);
            }
        }
        return  null;
    }
    private String checkRegular(JsonNode regular, String text){
        String res = text;
        if (regular.isArray()){
            Iterator<JsonNode> itr = regular.elements();
            while (itr.hasNext()){
                res = this.regularService.startRegular(itr.next().asText(), res);
            }
        }
        return res;
    }

    private List<Object> runPostgresqlService(JsonNode element) throws SQLException, IOException {
        String sql = checkRegular(element.get("regular"), element.get("sql").textValue());
        PostgresqlService postgresqlService = new PostgresqlService();
        List<Object> res = postgresqlService.runSql(sql);
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