package com.example.bec.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bec.configuration.PropertiesConfig;
import com.example.bec.enums.ConvertTypeEnum;
import com.example.bec.model.command.CommandConvertModel;
import com.example.bec.model.command.store.StoreCommandModel;
import com.example.bec.model.command.store.StoreFindCommandModel;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ConvertService {
    private final PropertiesConfig propertiesConfig;

    public ConvertService(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public String createToken(String email, String nik) throws IOException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email",email)
                .withClaim("nik", nik)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(this.propertiesConfig.getProperties().getProperty("token.secret")));
    }
    public String createMapKeyString(Map<String, Object> map){
        StringBuilder keyString = new StringBuilder();
        for(String key: map.keySet()){
            if (!keyString.toString().equals("")){
                keyString.append(",");
            }
            keyString.append(key);
        }
        return keyString.toString();
    }
    public String createMapValueString(Map<String, Object> map){
        StringBuilder keyString = new StringBuilder();
        for(Map.Entry<String, Object> value: map.entrySet()){
            if (!keyString.toString().equals("")){
                keyString.append(",");
            }
            if (value.getValue() instanceof String){
                keyString.append('\'').append(value.getValue().toString()).append('\'');
            }else {
                keyString.append(value.getValue());
            }

        }
        return keyString.toString();
    }

    public void convertConfig(CommandConvertModel convertModel, StoreCommandModel storeCommandModel, List<String> keys) throws IOException {
        Map<String, Object> data =  new HashMap<>();
        if (convertModel.getParams() != null){
            data = storeCommandModel.storeGetData(convertModel.getParams());
        }
        /* создать токен */
        Object res = null;
        if (convertModel.getType().equals(ConvertTypeEnum.createToken.getTitle())){
            res = this.createToken(
                    data.get("email").toString(),
                    data.get("nik").toString()
             );

        }
        /* добавить значение к начало переменной */
        else if (convertModel.getType().equals(ConvertTypeEnum.beforeAdd.getTitle())){
           res = data.get("value").toString() + storeCommandModel.searchValue(keys).toString();
        }
        /* добавить значение в конец переменной */
        else if(convertModel.getType().equals(ConvertTypeEnum.afterAdd.getTitle())){
            res = storeCommandModel.searchValue(keys).toString() + data.get("value").toString();
        }
        /* проверить кэш паролей */
        else if (convertModel.getType().equals(ConvertTypeEnum.checkPassword.getTitle())){
            res = this.checkPassword(
                    data.get("password").toString(),
                    data.get("hash").toString()
            );
        }
        /* сохранить кэш пароля */
        else if (convertModel.getType().equals(ConvertTypeEnum.hashPassword.getTitle())){
            res = this.hashPassword(
                    data.get("password").toString()
            );
        }
        /* сохранить константу */
        else if(convertModel.getType().equals(ConvertTypeEnum.constValue.getTitle())){
            res = data.get("const_value");
        }
        /* сохранить Map<String,Object> */
        else if(convertModel.getType().equals(ConvertTypeEnum.createMap.getTitle())){
            res = new HashMap<>(data);
        }
        /* create List */
        else if(convertModel.getType().equals(ConvertTypeEnum.createList.getTitle())){
            res = new ArrayList<>();
        }
        /* substring обрезать строку */
        else if(convertModel.getType().equals(ConvertTypeEnum.substring.getTitle())){
            res = data.get("value").toString().substring(
                    (Integer) data.get("start"),
                    data.get("value").toString().length() - (Integer) data.get("end")
            );
        }
        /* createMapKeyString */
        else if (convertModel.getType().equals(ConvertTypeEnum.createMapKeyString.getTitle())){
            res = this.createMapKeyString((Map<String, Object>) data.get("map"));
        }
        /* createMapKeyString */
        else if (convertModel.getType().equals(ConvertTypeEnum.createMapValueString.getTitle())){
            res = this.createMapValueString((Map<String, Object>) data.get("map"));
        }
        /* add_list */
        else if(convertModel.getType().equals(ConvertTypeEnum.addList.getTitle())){
            Object list = storeCommandModel.searchValue(keys);
            ((List<Object>) list).add(data.get("value"));
            return;
        }
        storeCommandModel.updateValue(
                keys,
                res
        );
    }

}
