package io.lambda.proxy.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * 
 * @author muditha
 *
 */
public class LambdaLogger {

    private static final String LOG_LEVEL = "loglevel";

    public static Logger getLogger(Class<?> clazz) {
        Level logLevel = System.getenv(LOG_LEVEL) == null ? Level.INFO : Level.toLevel(System.getenv(LOG_LEVEL));
        Configurator.setRootLevel(logLevel);
        return LogManager.getLogger(clazz);
    }
}