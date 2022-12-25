package com.example.bec.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthorizationService {
    private final CommandService commandService;

    public AuthorizationService(CommandService commandService) {
        this.commandService = commandService;
    }

    public Optional<Object> checkRight(String right, String token) throws SQLException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("right_const_name", right);
        params.put("token", token);
        Optional<Object> result = this.commandService.runCommand("check_right.json", params);
        if (result.isPresent()){
            if (
                    result.get() instanceof ResponseEntity<?> &&
                            !((ResponseEntity<?>) result.get()).getStatusCode().equals(HttpStatus.OK))
            {
                return result;
            }
            Object res = ((Map<?, ?>) result.get()).get("result_");
            res = ((Map<?, ?>) res).get("status_");
            if ( (int) res == 0){
                return  result;
            }
        }
        return  Optional.empty();
    }
}
