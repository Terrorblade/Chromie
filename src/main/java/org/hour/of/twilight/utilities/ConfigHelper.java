package org.hour.of.twilight.utilities;

import java.io.*;
import java.util.Properties;

public class ConfigHelper {
    public static String devRealmSoap;
    public static String liveRealmSoap;
    public static String botToken;
    public static String botUsername;
    public static String botPassword;
    public static Boolean updateCommands;
    public static void LoadConfig()
    {
        File configFile = new File("config.properties");
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            devRealmSoap = props.getProperty("devRealmSoap");
            liveRealmSoap = props.getProperty("liveRealmSoap");
            botToken = props.getProperty("botToken");
            botUsername = props.getProperty("botUsername");
            botPassword = props.getProperty("botPassword");
            updateCommands = Boolean.parseBoolean(props.getProperty("updateCommands", "false"));
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void UpdateConfig(String key, String value)
    {
        File configFile = new File("config.properties");
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            props.setProperty(key, value);
            props.store(new FileOutputStream("config.properties"), null);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
