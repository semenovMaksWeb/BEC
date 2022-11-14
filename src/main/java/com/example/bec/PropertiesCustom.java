package com.example.bec;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesCustom {
    static public  Properties getProperties() throws IOException {
        Properties property = new Properties();
        FileInputStream fis = new FileInputStream(".properties");
        property.load(fis);
        return  property;
    }
    static public  String  getName(String name) throws IOException {
        return getProperties().getProperty(name);
    }
}
