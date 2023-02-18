package com.example.bec.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/* old delete */
@Configuration
public class PropertiesConfig {
    private final static String NAME = "application.properties";
    @Bean
    public Properties getProperties() throws IOException {
        Properties property = new Properties();
        FileInputStream fis = new FileInputStream(NAME);
        property.load(fis);
        return property;
    }
}
