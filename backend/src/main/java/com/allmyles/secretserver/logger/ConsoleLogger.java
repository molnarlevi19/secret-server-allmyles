package com.allmyles.secretserver.logger;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The ConsoleLogger class implements the Logger interface and provides
 * logging functionality to the console.
 */
@Component
public class ConsoleLogger implements Logger {

    /**
     * Logs an informational message to the console.
     *
     * @param message The message to be logged.
     */
    @Override
    public void logInfo(String message) {
        String formattedMessage = formatLogMessage("INFO", message);
        System.out.println(formattedMessage);
    }

    /**
     * Logs an error message to the console.
     *
     * @param message The error message to be logged.
     */
    @Override
    public void logError(String message) {
        String formattedMessage = formatLogMessage("ERROR", message);
        System.err.println(formattedMessage);
    }

    /**
     * Formats a log message with the specified log level and message.
     *
     * @param logLevel The log level (e.g., "INFO" or "ERROR").
     * @param message  The message to be logged.
     * @return The formatted log message.
     */
    private String formatLogMessage(String logLevel, String message) {
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("[%s] [%s] - %s", formattedTimestamp, logLevel, message);
    }
}