package com.chernik.internetprovider.property;

import com.chernik.internetprovider.context.Component;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class PropertyHolderImpl implements PropertyHolder {
    private final static Logger LOGGER = LogManager.getLogger(PropertyHolderImpl.class);
    private static final String PROPERTIES_FILE_NAME = "application";
    private Map<String, String> properties;

    public PropertyHolderImpl() {
        initProperties();
    }

    public String getProperty(String propertyName) {
        LOGGER.log(Level.TRACE, "Getting property {}", propertyName);
        return properties.get(propertyName);
    }

    private void initProperties() {
        properties = new HashMap<>();

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
