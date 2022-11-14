package com.example.bec.service;

import com.example.bec.PropertiesCustom;

import java.io.FileInputStream;
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
    public List<Object>  runSql(String sql) throws SQLException {
        Statement stmt = this.conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
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