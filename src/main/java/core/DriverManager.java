package core;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

/**
 * Thread-safe WebDriver manager using ThreadLocal storage.
 * Each test thread gets its own isolated WebDriver instance.
 */
@Slf4j
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
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
            return;
        }
        try {
            log.debug("Quitting WebDriver for thread '{}'", Thread.currentThread().getName());
            driver.quit();
            log.info("WebDriver session quit successfully for thread '{}'", Thread.currentThread().getName());
        } catch (Exception e) {
            log.warn("Exception while quitting WebDriver for thread '{}': {}", Thread.currentThread().getName(), e.getMessage());
        } finally {
            DRIVER.remove();
        }
    }
}