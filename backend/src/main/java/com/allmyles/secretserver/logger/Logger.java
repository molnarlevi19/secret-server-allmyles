package com.allmyles.secretserver.logger;

/**
 * The Logger interface defines methods for logging messages.
 * Implementing classes should provide concrete implementations
 * for logging informational and error messages.
 */
public interface Logger {

    /**
     * Logs an informational message.
     *
     * @param message The message to be logged.
     */
    void logInfo(String message);

    /**
     * Logs an error message.
     *
     * @param message The error message to be logged.
     */
    void logError(String message);
}
