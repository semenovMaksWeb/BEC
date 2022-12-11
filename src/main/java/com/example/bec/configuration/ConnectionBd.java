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
    private final PropertiesCustom propertiesCustom;

    public ConnectionBd(PropertiesCustom propertiesCustom) {
        this.propertiesCustom = propertiesCustom;
    }

    @Bean
    public Connection postgresqlConnection() throws IOException, SQLException {
        String url = "jdbc:postgresql://" + propertiesCustom.getProperties().getProperty("db.host");
        Properties props = new Properties();
        props.setProperty("user", propertiesCustom.getProperties().getProperty("db.user"));
        props.setProperty("password", propertiesCustom.getProperties().getProperty("db.password"));
        return  DriverManager.getConnection(url, props);
    }
}
