package com.accomputers.api.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.accomputers.api.application.ports.output.LoggerPort;

/**
 * Adapter implementation of LoggerPort following hexagonal architecture.
 * This adapter bridges the application layer's logging needs with the
 * infrastructure layer's SLF4J logging implementation.
 * 
 * The MDC (Mapped Diagnostic Context) is automatically handled by SLF4J
 * and is set per request by the LoggingContextFilter, so we don't need
 * to manually pass context information.
 */
@Component
public class LogAdapter implements LoggerPort {

    private static final String LOGGER_NAME = "ApplicationLogger";
    private final Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

    @Override
    public void emergency(String message) {
        logger.error("[EMERGENCY] {}", message);
    }

    @Override
    public void alert(String message) {
        logger.error("[ALERT] {}", message);
    }

    @Override
    public void critical(String message) {
        logger.error("[CRITICAL] {}", message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error("[ERROR] {}", message, throwable);
    }

    @Override
    public void warning(String message) {
        logger.warn("[WARNING] {}", message);
    }

    @Override
    public void notice(String message) {
        logger.info("[NOTICE] {}", message);
    }

    @Override
    public void info(String message) {
        logger.info("[INFO] {}", message);
    }

    @Override
    public void debug(String message) {
        logger.debug("[DEBUG] {}", message);
    }

    @Override
    public void trace(String message) {
        logger.trace("[TRACE] {}", message);
    }
}
