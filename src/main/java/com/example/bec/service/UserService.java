package com.example.bec.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final CommandService commandService;

    public UserService(CommandService commandService) {
        this.commandService = commandService;
    }

    public Optional<Object> confirmedUser(String hash, String id) throws SQLException, MessagingException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("hash", hash);
        params.put("id_user", Integer.parseInt(id));
        return this.commandService.runCommand("confirmed_user.json", params);
    }
}
