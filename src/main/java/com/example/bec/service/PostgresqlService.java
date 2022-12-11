package com.example.bec.service;

import com.example.bec.PropertiesCustom;
import com.example.bec.enums.VarTypeEnum;
import com.example.bec.model.command.CommandSqlModel;
import com.example.bec.model.command.SqlParamsModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;


import java.io.IOException;
import java.sql.*;
import java.util.*;

public class PostgresqlService {
    /* todo попытаться сделать сервисом */
    public final Connection conn;

    /* todo перенести в конфигурацию */
    public PostgresqlService() throws SQLException, IOException {
        Properties property = PropertiesCustom.getProperties();
        String  url = "jdbc:postgresql://" + property.getProperty("db.host");
        Properties props = new Properties();
        props.setProperty("user", property.getProperty("db.user"));
        props.setProperty("password", property.getProperty("db.password"));
        this.conn = DriverManager.getConnection(url, props);
    }
    /* todo перенести в конфигурацию */
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
    ) throws SQLException, JsonProcessingException {
        PreparedStatement statement = this.conn.prepareStatement(commandSql.getText());
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

    public void closeConn() throws SQLException {
        this.conn.close();
    }
}