package com.example.bec.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bec.PropertiesCustom;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class ConvertService {
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public String createToken(Map<String, Object> params) throws IOException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", params.get("email").toString())
                .withClaim("nik", params.get("nik").toString())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(PropertiesCustom.getName("token.secret")));
    }
}
