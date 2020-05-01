import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

import static java.lang.String.format;

/**
 * @author Saurav Singh
 **/
public final class PropertiesProcessor {
    private static Logger logger = LoggerFactory.getLogger(PropertiesProcessor.class);

    // Lazy initialization of properties.
    private static volatile Properties properties;

    private final static String DEFAULT_PROFILE = "default";

    private final static String DEFAULT_PROP_FILE = "application.yml";

    private final static String DEFAULT_PROP_FORMAT = "application-%s.yml";


    private static Object mutex = new Object();

    public static Properties getProperties() {
        Properties result = properties;
        if (result == null) {
            synchronized (mutex) {
                result = properties;
                if (result == null)
                    properties = result = new Properties();
            }
        }
        return result;
    }

    static {
        String activeProfile = System.getProperty("active.profile");
        if (null == activeProfile || activeProfile.isEmpty()) {
            activeProfile = DEFAULT_PROFILE;
        }

        // enforced to use standard naming convention and allow dev flexibility.
        activeProfile = activeProfile.toLowerCase();
        String propertyFile = activeProfile.equals("default") ? DEFAULT_PROP_FILE : format(DEFAULT_PROP_FORMAT, activeProfile);
        if (logger.isDebugEnabled()) {
            logger.debug("Started loading with active profile {}.", activeProfile);
        }
        try {
            final YamlProcessor yamlProcessor = new YamlProcessor();
            properties = new Properties();
            final InputStream inputStream = PropertiesProcessor.class.getResourceAsStream("/" + propertyFile);
            yamlProcessor.process((prop, map) -> properties.putAll(prop), inputStream);
        } catch (Exception e) {
            logger.error("Failed to load properties file {}.", propertyFile);
            throw new RuntimeException("Failed to load properties file.", e);
        }
    }

    public static String getProperty(final String key) {
        if ((null == key || key.isEmpty()) && null == properties) {
            return null;
        }
        return properties.getProperty(key);
    }
}
