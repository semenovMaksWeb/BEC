package com.example.bec.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class ConnectionBd {

    private final static String USER = "user";
    private final static String PASSWORD = "password";
    private final Environment env;

    public ConnectionBd(Environment env) {
        this.env = env;
    }

    @Bean
    public Connection postgresqlConnection() throws SQLException {
        String url = "jdbc:postgresql://" + this.env.getProperty("db.host");
        Properties props = new Properties();
        props.setProperty(USER, this.env.getProperty("db.user"));
        props.setProperty(PASSWORD, this.env.getProperty("db.password"));
        return DriverManager.getConnection(url, props);
    }
}
