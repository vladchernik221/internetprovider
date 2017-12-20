package com.chernik.internetprovider.property;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PropertyHolder {
    private final static Logger LOGGER = LogManager.getLogger(PropertyHolder.class);
    private static final String PROPERTIES_FILE_PATH = "/application.properties";
    private static final String PROPERTIES_FILE_NAME = "application";
    private static Map<String, String> properties;

    public String getProperty(String propertyName) {
        if (properties == null) {
            initProperties();
        }
        LOGGER.log(Level.TRACE, "Getting property {}", propertyName);
        return properties.get(propertyName);
    }

    private void initProperties() {
        properties = new HashMap<>();

        if (Thread.currentThread().getClass().getResource(PROPERTIES_FILE_PATH) == null) {
            throw new RuntimeException(String.format("File %s is missing", PROPERTIES_FILE_PATH));
        }

        ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME);
        Enumeration bundleKeys = resourceBundle.getKeys();

        while (bundleKeys.hasMoreElements()) {
            String key = (String) bundleKeys.nextElement();
            String value = resourceBundle.getString(key);
            properties.put(key, value);
        }
        LOGGER.log(Level.INFO, "Properties were initialized");
    }
}
