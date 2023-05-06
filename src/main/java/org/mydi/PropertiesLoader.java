package org.mydi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties load(String filename) throws FileNotFoundException, IOException {
        var filestream = new FileInputStream(filename);
        var properties = new Properties();
        properties.load(filestream);
        return properties;
    }
}
