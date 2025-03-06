package util.logger;

import util.config.AppConfig;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MyLogger {
    private final Logger logger;
    private Level level;

    public MyLogger(Class<?> clazz, AppConfig config) {
        logger = Logger.getLogger(clazz.getName());

        switch (config.getMode()) {
            case "DEV":
                this.level = Level.INFO;
                break;
            case "PROD":
                this.level = Level.SEVERE;
                break;
            default:
                this.level = Level.INFO;
        }
        logger.setLevel(this.level);
    }

    public void log(Level level, String msg) {
        LogRecord record = new LogRecord(level, msg);
        record.setSourceClassName(logger.getName());
        logger.log(record);
    }

    public void info(String msg) {
        log(Level.INFO, msg);
    }

    public void severe(String msg) {
        log(Level.SEVERE, msg);
    }
}
