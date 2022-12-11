package com.example.bec.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bec.configuration.PropertiesCustom;
import com.example.bec.model.command.convert.ConvertModel;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConvertService {
    private final PropertiesCustom propertiesCustom;

    public ConvertService(PropertiesCustom propertiesCustom) {
        this.propertiesCustom = propertiesCustom;
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
   /* todo попытатся из класса ConvertParamsLinkModel создать метод который будет принимать data и возвращать элемент key:value  */
    public String createToken(Map<String, Object> params, Map<String, Object> dataset, ConvertModel convertModel) throws IOException {
        Map<String, Object> data = new HashMap<>();
       convertModel.getParams().getLink().getObjectParams(data,params);
        convertModel.getParams().getLink().getObjectDataset(data,dataset);
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", data.get("email").toString())
                .withClaim("nik", data.get("nik").toString())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(this.propertiesCustom.getProperties().getProperty("token.secret")));
    }

    public Object saveValue(ConvertModel convertModel){
        return convertModel.getParams().getValue();
    }
}
