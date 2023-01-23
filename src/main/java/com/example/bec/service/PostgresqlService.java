package com.example.bec.service;

import com.example.bec.configuration.ConnectionBd;
import com.example.bec.enums.VarTypeEnum;
import com.example.bec.model.command.sql.SqlModel;
import com.example.bec.model.command.sql.SqlParamsModel;
import com.example.bec.model.command.store.StoreCommandModel;
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
    private void StatementSave(
            PreparedStatement statement,
            List<SqlParamsModel> sqlParamsList,
            StoreCommandModel storeCommandModel
    ) throws SQLException {
        if (sqlParamsList == null){
            return;
        }
        for (SqlParamsModel sqlParam : sqlParamsList) {
            Object data = storeCommandModel.storeGetData(sqlParam.getData());
            /* это строка */
            if (Objects.equals(sqlParam.getType(), VarTypeEnum.string.getTitle())) {
                statement.setString(sqlParam.getIndex(), data.toString());
            }
            /* это число */
            else if (Objects.equals(sqlParam.getType(), VarTypeEnum.integer.getTitle())) {
                statement.setInt(sqlParam.getIndex(), (Integer) data);
            }
        }
    }

    public Object runSql(
            SqlModel sqlModel,
            StoreCommandModel storeCommandModel
    ) throws SQLException, IOException {
        PreparedStatement statement = this.connectionBd.postgresqlConnection().prepareStatement(sqlModel.getText());
        StatementSave(statement, sqlModel.getParams(), storeCommandModel);
        ResultSet rs = statement.executeQuery();
        return convertRsInJson(rs, sqlModel.getConvert());
    }

    private Object convertRsInJson(ResultSet rs, String typeConvert) throws SQLException, JsonProcessingException {
        List<Object> result = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++){
                /* это массив */
                if (Objects.equals(metaData.getColumnClassName(i), "java.sql.Array")){
                    map.put(metaData.getColumnName(i), rs.getArray(i).getArray());
                }
                /* это json */
                else if (rs.getObject(i) != null && rs.getObject(i) instanceof PGobject ){
                   PGobject pGobject = (PGobject) rs.getObject(i);
                   map.put(metaData.getColumnName(i),new ObjectMapper().readValue(pGobject.getValue(), Object.class));
                }
                /* это примитив */
                else {
                    map.put(metaData.getColumnName(i), rs.getObject(i));
                }
            }
            result.add(map);
        }
        if (Objects.equals(typeConvert, "object")){
            return result.get(0);
        }
        return result;
    }
}