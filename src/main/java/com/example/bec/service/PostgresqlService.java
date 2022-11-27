package com.example.bec.service;

import com.example.bec.PropertiesCustom;
import com.example.bec.enums.VarTypeEnum;
import com.example.bec.model.command.CommandSqlModel;
import com.example.bec.model.command.SqlOutModel;
import com.example.bec.model.command.SqlParamsModel;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class PostgresqlService {
    public final Connection conn;
    private final ChildrenDataService childrenDataService = new ChildrenDataService();
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
            Object data = childrenDataService.searchData(params, element);
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
    ) throws SQLException {
        PreparedStatement statement = this.conn.prepareStatement(commandSql.getText());
        StatementSave(statement, commandSql.getParams(), params);
        StatementSave(statement, commandSql.getDataset(), dataset);
        ResultSet rs = statement.executeQuery();
        return convertRsInJson(rs, commandSql.getConvert());
    }

    private Object convertRsInJson(ResultSet rs, String typeConvert) throws SQLException {
        List<Object> result = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++){
                /** TODO временное решения для теста */
                if (metaData.getColumnTypeName(i).equals("result_type")){
                    this.convertOutInJson(metaData, rs, i);
                }
                if (Objects.equals(metaData.getColumnClassName(i), "java.sql.Array")){
                    map.put(metaData.getColumnName(i), rs.getArray(i).getArray());
                }else {
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
    private void convertOutInJson(ResultSetMetaData metaData, ResultSet rs , Integer i) throws SQLException {
        List<SqlOutModel> outModelList = generationOutModel(metaData.getColumnTypeName(i));
        for (SqlOutModel outModel: outModelList){
            System.out.println(rs.getObject(i));

//            ResultSet rsObject = rs.getArray(i).getResultSet();
//            while (rsObject.next()) {
//                System.out.println(rsObject.getObject(0));
//                System.out.println(rsObject.getObject(1));
//            }
        }
    }
    private List<SqlOutModel> generationOutModel(String type) throws SQLException {
        List<SqlOutModel> outModelList = new ArrayList<>();
        ResultSet typeOut = getType(type);
        while (typeOut.next()) {
            SqlOutModel outModel = new SqlOutModel();
            outModel.setName(typeOut.getString(1));
            outModel.setName(typeOut.getString(2));
            outModelList.add(outModel);
        }
        return outModelList;
    }

    private ResultSet getType(String type) throws SQLException {
        PreparedStatement statement = this.conn.prepareStatement("select att.attname as name, pg_catalog.format_type(atttypid, NULL) as type from pg_attribute att join pg_class tbl on tbl.oid = att.attrelid join pg_namespace ns on tbl.relnamespace = ns.oid where tbl.relname = ?");
        statement.setString(1, type);
        return statement.executeQuery();
    }

    public void closeConn() throws SQLException {
        this.conn.close();
    }
}