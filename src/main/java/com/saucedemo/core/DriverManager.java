package com.saucedemo.core;

import com.saucedemo.exceptions.FrameworkException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

/**
 * Thread-safe WebDriver manager using ThreadLocal storage.
 * Each test thread gets its own isolated WebDriver instance.
 * Supports parallel test execution with proper resource cleanup.
 */
@Slf4j
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
        // Utility class constructor to prevent instantiation
        throw new UnsupportedOperationException("Utility class");
    }

    public static void set(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Cannot store a null WebDriver in DriverManager");
        }
        log.debug("Storing WebDriver [{}] for thread '{}'", driver.getClass().getSimpleName(), Thread.currentThread().getName());
        DRIVER.set(driver);
    }

    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No WebDriver found for thread '" + Thread.currentThread().getName() + "'");
        }
        return driver;
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            log.debug("No WebDriver to quit for thread '{}'", Thread.currentThread().getName());
            return;
        }
        try {
            log.debug("Quitting WebDriver for thread '{}'", Thread.currentThread().getName());
            driver.quit();
            log.info("WebDriver session quit successfully for thread '{}'", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("Exception while quitting WebDriver for thread '{}': {}", Thread.currentThread().getName(), e.getMessage(), e);
            throw new FrameworkException("Failed to quit WebDriver for thread '" + Thread.currentThread().getName() + "'", e);
        } finally {
            DRIVER.remove();
        }
    }
}