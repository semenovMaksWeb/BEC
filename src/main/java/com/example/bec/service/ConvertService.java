package com.example.bec.service;


import org.mindrot.jbcrypt.BCrypt;

public class ConvertService {
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
