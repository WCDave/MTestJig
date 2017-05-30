
package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConfigurationManager extends Properties {

  private static ConfigurationManager instance;
  private Logger log;
  private Properties sysProperties;


  public ConfigurationManager() {
    super();
    init();

  }

  private void init() {
    log = Logger.getLogger(ConfigurationManager.class);
    log.debug("Config file loading");

    sysProperties = System.getProperties();
    String configFilePath = sysProperties.getProperty("dave.config.path", ".");

    ResourceBundle rb = ResourceBundle.getBundle("config");

    InputStream in = null;
    boolean usingXML = true;
    try {
      in = new FileInputStream(configFilePath + "\\Config.xml");
    } catch (FileNotFoundException e1) {
      try {
        usingXML = false;
        log.warn("No config.xml file found...trying config.properties");
        System.out.println("No config.xml file found...trying config.properties");
        in = new FileInputStream("Config.properties");
      } catch (FileNotFoundException e) {
        log.warn("No config.properties file found");
      }
    }

    try {
      if (in != null) {
        if (usingXML) {
          log.debug("using " + configFilePath + "\\Config.xml");
          loadFromXML(in);
        } else {
          log.debug("using Config.properties");
          load(in);
        }
      }
    } catch (InvalidPropertiesFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        in = null;
      }
    }
  }

  public static ConfigurationManager getInstance() {
    if (instance == null) {
      instance = new ConfigurationManager();
    }
    return instance;
  }

  /**
   * @return the sysProperties
   */
  public Properties getSysProperties() {
    return sysProperties;
  }

}
