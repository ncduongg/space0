package com.space;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private static final String FILE_CONFIG = "/src/main/java/com/space/config.properties";
    private static InputStream inputStream = null;
    private static Properties p = new Properties();
    private static Logger logger = Logger.getLogger(Config.class.getName());

    public static Properties gProperties() {
        try {
            String currentDir = System.getProperty("user.dir");
            inputStream = new FileInputStream(currentDir + FILE_CONFIG);
            p.load(inputStream);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] gProperties ", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }
}
