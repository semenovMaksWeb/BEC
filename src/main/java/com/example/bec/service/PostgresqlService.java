package com.example.bec.service;

import com.example.bec.PropertiesCustom;
import com.example.bec.enums.VarTypeEnum;
import com.example.bec.model.command.sql.SqlParams;

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
    public List<Object> runSql(String sql, List<SqlParams> config, Map<String, Object> params) throws SQLException {
        PreparedStatement statement = this.conn.prepareStatement(sql);
        int index = 0;
        for (SqlParams element : config) {
            index++;
            if (Objects.equals(element.getType(), VarTypeEnum.string.getTitle())) {
                statement.setString(index, params.get(element.getKey()).toString());
            } else if (Objects.equals(element.getType(), VarTypeEnum.integer.getTitle())) {
                statement.setInt(index, (Integer) params.get(element.getKey()));
            }
        }
        ResultSet rs = statement.executeQuery();
        return convertRsInJson(rs);
    }

    private List<Object> convertRsInJson(ResultSet rs) throws SQLException {
        List<Object> result = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++){
                map.put(metaData.getColumnName(i), rs.getObject(i));
            }
            result.add(map);
        }
        return  result;
    }
    public void closeConn() throws SQLException {
        this.conn.close();
    }
}