package com.example.bec.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bec.configuration.PropertiesConfig;
import com.example.bec.enums.ConvertTypeEnum;
import com.example.bec.model.command.CommandConvertModel;
import com.example.bec.model.command.store.StoreCommandModel;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        System.out.println(1);
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email",email)
                .withClaim("nik", nik)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(this.propertiesConfig.getProperties().getProperty("token.secret")));
    }
    public void convertConfig(CommandConvertModel convertModel, StoreCommandModel storeCommandModel, List<String> keys) throws IOException {
        Map<String, Object> data = storeCommandModel.storeGetData(convertModel.getParams());
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
           res = storeCommandModel.searchValue(keys).toString() + data.get("value").toString();
        }
        /* проверить кэш паролей */
        else if (convertModel.getType().equals(ConvertTypeEnum.checkPassword.getTitle())){
            res = this.checkPassword(
                    data.get("password").toString(),
                    data.get("hash").toString()
            );
        }
        /* проверить кэш паролей */
        else if (convertModel.getType().equals(ConvertTypeEnum.hashPassword.getTitle())){
            res = this.hashPassword(
                    data.get("password").toString()
            );
        }

        storeCommandModel.updateValue(
                keys,
                res
        );
    }

}
