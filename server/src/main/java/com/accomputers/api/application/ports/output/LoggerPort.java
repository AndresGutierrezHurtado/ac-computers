package com.accomputers.api.application.ports.output;

/**
 * Port for logging operations following hexagonal architecture.
 * This is an output port that defines the contract for logging functionality
 * that the application layer requires from the infrastructure layer.
 */
public interface LoggerPort {
    
    /**
     * Logs an emergency message (system is unusable)
     * @param message the message to log
     */
    void emergency(String message);
    
    /**
     * Logs an alert message (action must be taken immediately)
     * @param message the message to log
     */
    void alert(String message);
    
    /**
     * Logs a critical message (critical conditions)
     * @param message the message to log
     */
    void critical(String message);
    
    /**
     * Logs an error message with exception
     * @param message the message to log
     * @param throwable the exception to log
     */
    void error(String message, Throwable throwable);
    
    /**
     * Logs a warning message
     * @param message the message to log
     */
    void warning(String message);
    
    /**
     * Logs a notice message (normal but significant condition)
     * @param message the message to log
     */
    void notice(String message);
    
    /**
     * Logs an informational message
     * @param message the message to log
     */
    void info(String message);
    
    /**
     * Logs a debug message
     * @param message the message to log
     */
    void debug(String message);
    
    /**
     * Logs a trace message (very detailed information)
     * @param message the message to log
     */
    void trace(String message);
}
