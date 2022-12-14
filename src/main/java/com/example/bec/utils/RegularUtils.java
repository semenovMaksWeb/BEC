package com.example.bec.utils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {
    Map<String, Object> params;
    public RegularUtils(Map<String, Object> params) {
        this.params = params;
    }

    public String  startRegular(String name, String  text){
        if ("var".equals(name)) {
            return regularVar(text);
        }
        return text;
    }
    public String regularVar(String text){

        Matcher matcher = Pattern.compile("%\\w+%").matcher(text);
        AtomicReference<String> result = new AtomicReference<>();
        matcher.results().forEach(r -> {
           String name = r.group();
           String key = name.substring(1, name.length() - 1);
           result.set(text.replaceAll(name, params.get(key).toString()));
        });
        return result.get();
    }
}
