package com.example.bec.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bec.configuration.PropertiesCustom;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
public class ConvertService {
    private final PropertiesCustom propertiesCustom;

    public ConvertService(PropertiesCustom propertiesCustom) {
        this.propertiesCustom = propertiesCustom;
    }

    public String hashPassword(Map<String, Object> data) {
        return BCrypt.hashpw(data.get("password").toString(), BCrypt.gensalt());
    }

    public boolean checkPassword(Map<String, Object> data) {
        return BCrypt.checkpw(data.get("password").toString(), data.get("hash").toString());
    }

    public String createToken(Map<String, Object> data) throws IOException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", data.get("email").toString())
                .withClaim("nik", data.get("nik").toString())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(this.propertiesCustom.getProperties().getProperty("token.secret")));
    }

}
