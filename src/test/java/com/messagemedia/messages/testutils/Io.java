package com.messagemedia.messages.testutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Io {

    public static Properties getResourceProperties(String propertiesFileResourcePath) {

        ClassLoader classLoader = Io.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(propertiesFileResourcePath)) {
             Properties properties = new Properties();
             properties.load(inputStream);
             return properties;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
