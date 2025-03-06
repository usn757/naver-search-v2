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
        String mode = config.getMode();
        String logLevel = config.getLogLevel(); // LOG_LEVEL 환경변수 추가.

        // 명시적으로 LOG_LEVEL이 설정된 경우, 해당 레벨 사용
        if (logLevel != null && !logLevel.isBlank()) {
            try {
                this.level = Level.parse(logLevel);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid LOG_LEVEL: " + logLevel + ". Using INFO as default.");
                this.level = Level.INFO; // 유효하지 않은 LOG_LEVEL이면 기본값 사용.
            }
        } else { // LOG_LEVEL이 설정되지 않은 경우, MODE에 따라 설정
            switch (mode) {
                case "DEV":
                    this.level = Level.INFO;
                    break;
                case "PROD":
                    this.level = Level.SEVERE;
                    break;
                default:
                    this.level = Level.INFO;
            }
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
