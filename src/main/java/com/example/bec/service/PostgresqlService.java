package com.example.bec.service;

import com.example.bec.configuration.ConnectionBd;
import com.example.bec.enums.VarTypeEnum;
import com.example.bec.model.command.sql.CommandSqlModel;
import com.example.bec.model.command.sql.SqlParamsModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.postgresql.util.PGobject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@Service
public class PostgresqlService {

    private final ConnectionBd connectionBd;

    public PostgresqlService(ConnectionBd connectionBd) {
       this.connectionBd = connectionBd;
    }
    private void StatementSave(PreparedStatement statement, List<SqlParamsModel> config, Map<String, Object> params) throws SQLException {
        if (config == null){
            return;
        }
        for (SqlParamsModel element : config) {
            Object data = element.searchData(params);
            if (Objects.equals(element.getType(), VarTypeEnum.string.getTitle())) {
                statement.setString(element.getIndex(), data.toString());
            } else if (Objects.equals(element.getType(), VarTypeEnum.integer.getTitle())) {
                statement.setInt(element.getIndex(), (Integer) data);
            }
        }
    }

    public Object runSql(
            CommandSqlModel commandSql,
            Map<String, Object> params,
            Map<String, Object> dataset
    ) throws SQLException, IOException {
        PreparedStatement statement = this.connectionBd.postgresqlConnection().prepareStatement(commandSql.getText());
        StatementSave(statement, commandSql.getParams(), params);
        StatementSave(statement, commandSql.getDataset(), dataset);
        ResultSet rs = statement.executeQuery();
        return convertRsInJson(rs, commandSql.getConvert());
    }

    private Object convertRsInJson(ResultSet rs, String typeConvert) throws SQLException, JsonProcessingException {
        List<Object> result = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++){
                /** это массив */
                if (Objects.equals(metaData.getColumnClassName(i), "java.sql.Array")){
                    map.put(metaData.getColumnName(i), rs.getArray(i).getArray());
                }
                /** это json */
                else if (rs.getObject(i) != null && rs.getObject(i).getClass().getSimpleName().equals("PGobject")){
                   PGobject pGobject = (PGobject) rs.getObject(i);
                   map.put(metaData.getColumnName(i),new ObjectMapper().readValue(pGobject.getValue(), Object.class));
                }
                /** это примитив */
                else {
                    map.put(metaData.getColumnName(i), rs.getObject(i));
                }
            }
            result.add(map);
        }
        if (Objects.equals(typeConvert, "object")){
            return result.get(0);
        }
        return  result;
    }
}