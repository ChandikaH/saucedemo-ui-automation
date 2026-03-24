package com.saucedemo.core;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public final class DriverFactory {

    private DriverFactory() {
        // Add this to prevent instantiation (Sonar rule)
        throw new UnsupportedOperationException("Utility class");
    }

    public static void initDriver() {
        WebDriver driver = WebDriverFactory.create();
        DriverManager.set(driver);
        log.info("WebDriver initialised and stored for thread '{}'", Thread.currentThread().getName());
    }

    public static void tearDownDriver() {
        log.info("Tearing down WebDriver for thread '{}'",
                Thread.currentThread().getName());
        DriverManager.quit();
    }
}