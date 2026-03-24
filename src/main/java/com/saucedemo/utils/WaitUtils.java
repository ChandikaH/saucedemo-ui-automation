package com.saucedemo.utils;

import com.saucedemo.config.ConfigLoader;
import com.saucedemo.core.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
public final class WaitUtils {

    private static final int DEFAULT_TIMEOUT_SECONDS = ConfigLoader.get().getTimeouts().getExplicit();

    private WaitUtils() {
        // Utility class constructor to prevent instantiation
        throw new UnsupportedOperationException("Utility class");
    }

    public static WebDriverWait defaultWait() {
        return new WebDriverWait(
                DriverManager.get(),
                Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS),
                Duration.ofMillis(300)
        );
    }

    public static WebDriverWait customWait(int timeoutSeconds) {
        return new WebDriverWait(
                DriverManager.get(),
                Duration.ofSeconds(timeoutSeconds),
                Duration.ofMillis(300)
        );
    }

    public static WebElement waitForVisibility(By locator) {
        log.debug("Waiting for visibility of: {}", locator);
        return defaultWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(WebElement element) {
        log.debug("Waiting for visibility of element");
        return defaultWait().until(
                ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickability(By locator) {
        log.debug("Waiting for clickability of: {}", locator);
        return defaultWait().until(
                ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickability(WebElement element) {
        return defaultWait().until(
                ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForPresence(By locator) {
        log.debug("Waiting for DOM presence of: {}", locator);
        return defaultWait().until(
                ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static boolean waitForText(By locator, String expectedText) {
        log.debug("Waiting for text '{}' in: {}", expectedText, locator);
        return defaultWait().until(
                ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
    }

    public static void waitForUrlContains(String urlFragment) {
        log.debug("Waiting for URL to contain: '{}'", urlFragment);
        defaultWait().until(ExpectedConditions.urlContains(urlFragment));
    }

    public static boolean waitForInvisibility(By locator) {
        log.debug("Waiting for invisibility of: {}", locator);
        return defaultWait().until(
                ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebDriverWait waitFor(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
}