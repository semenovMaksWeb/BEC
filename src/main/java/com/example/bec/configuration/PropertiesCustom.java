package com.example.bec.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class PropertiesCustom {
    @Bean
    public Properties getProperties() throws IOException {
        Properties property = new Properties();
        FileInputStream fis = new FileInputStream(".properties");
        property.load(fis);
        return  property;
    }
}
