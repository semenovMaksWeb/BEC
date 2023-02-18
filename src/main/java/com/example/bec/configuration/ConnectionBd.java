package com.example.bec.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class ConnectionBd {
    private final PropertiesConfig propertiesConfig;
    private final static String USER = "user";
    private final static String PASSWORD = "password";

    public ConnectionBd(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    @Bean
    public Connection postgresqlConnection() throws IOException, SQLException {
        String url = "jdbc:postgresql://" + propertiesConfig.getProperties().getProperty("db.host");
        Properties props = new Properties();
        props.setProperty(USER, propertiesConfig.getProperties().getProperty("db.user"));
        props.setProperty(PASSWORD, propertiesConfig.getProperties().getProperty("db.password"));
        return DriverManager.getConnection(url, props);
    }
}
