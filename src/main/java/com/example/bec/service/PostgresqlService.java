package com.example.bec.service;

import com.example.bec.PropertiesCustom;
import com.example.bec.enums.VarTypeEnum;
import com.example.bec.model.command.CommandSqlModel;
import com.example.bec.model.command.SqlParamsModel;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class PostgresqlService {
    private final Connection conn;
    public PostgresqlService() throws SQLException, IOException {
        Properties property = PropertiesCustom.getProperties();
        String  url = "jdbc:postgresql://" + property.getProperty("db.host");
        Properties props = new Properties();
        props.setProperty("user", property.getProperty("db.user"));
        props.setProperty("password", property.getProperty("db.password"));
        this.conn = DriverManager.getConnection(url, props);
    }
    private void StatementSave(PreparedStatement statement, List<SqlParamsModel> config, Map<String, Object> params) throws SQLException {
        if (config == null){
            return;
        }
        for (SqlParamsModel element : config) {
            if (Objects.equals(element.getType(), VarTypeEnum.string.getTitle())) {
                statement.setString(element.getIndex(), params.get(element.getKey()).toString());
            } else if (Objects.equals(element.getType(), VarTypeEnum.integer.getTitle())) {
                statement.setInt(element.getIndex(), (Integer) params.get(element.getKey()));
            }
        }
    }

    public Object runSql(
            CommandSqlModel commandSql,
            Map<String, Object> params,
            Map<String, Object> dataset
    ) throws SQLException {
        PreparedStatement statement = this.conn.prepareStatement(commandSql.getText());
        StatementSave(statement, commandSql.getParams(), params);
        StatementSave(statement, commandSql.getDataset(), dataset);
        ResultSet rs = statement.executeQuery();
        return convertRsInJson(rs, commandSql);
    }

    private Object convertRsInJson(ResultSet rs, CommandSqlModel commandSqlModel) throws SQLException {
        List<Object> result = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++){
                if (Objects.equals(metaData.getColumnClassName(i), "java.sql.Array")){
                    map.put(metaData.getColumnName(i), rs.getArray(i).getArray());
                }else {
                    map.put(metaData.getColumnName(i), rs.getObject(i));
                }
            }
            result.add(map);
        }
        if (Objects.equals(commandSqlModel.getConvert(), "object")){
            return result.get(0);
        }
        return  result;
    }

    public void closeConn() throws SQLException {
        this.conn.close();
    }
}