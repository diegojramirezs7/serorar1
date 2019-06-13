package de.uniba.dsg;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {
    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    public static Properties loadProperties() {
        try (InputStream stream = CustomSpotifyApi.class.getClassLoader().getResourceAsStream("config.properties")) {
            //properties is a subclass of Hashtable, holds a list of key:value pairs
        	//multiple threads can share a single properties object without need of external synchronization
        	//this object will just hold important values for configuration in both servers. 
        	Properties properties = new Properties();
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            LOGGER.severe("Cannot load configuration file.");
            return null;
        }
    }
}
