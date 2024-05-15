package com.example.momentumv1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Appconfig {
    private static final String PROPERTIES_FILE = "src/main/java/com/example/momentumv1/db_config.properties";
    private static final Properties properties = loadProperties();

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    public static String getDbPassword() {
        return properties.getProperty("db_password");
    }

    public static String getDbUsername() {
        return properties.getProperty("db_user");
    }

    public static String getDbUrl() {
        return properties.getProperty("db_url");
    }

}
