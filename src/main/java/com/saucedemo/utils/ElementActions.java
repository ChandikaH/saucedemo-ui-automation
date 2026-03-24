package com.saucedemo.utils;

import com.saucedemo.core.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

@Slf4j
public final class ElementActions {

    private ElementActions() {
        // Utility class constructor to prevent instantiation
        throw new UnsupportedOperationException("Utility class");
    }

    public static void click(By locator) {
        log.debug("Clicking element: {}", locator);
        WaitUtils.waitForClickability(locator).click();
    }

    public static void jsClick(By locator) {
        log.debug("JS-clicking element: {}", locator);
        WebElement element = WaitUtils.waitForPresence(locator);
        ((JavascriptExecutor) DriverManager.get()).executeScript("arguments[0].click();", element);
    }

    public static void type(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement input = WaitUtils.waitForVisibility(locator);
        input.clear();
        input.sendKeys(text);
    }

    public static String getText(By locator) {
        String text = WaitUtils.waitForVisibility(locator).getText();
        log.debug("getText({}) → '{}'", locator, text);
        return text;
    }

    public static String getAttribute(By locator, String attribute) {
        String value = WaitUtils.waitForVisibility(locator).getAttribute(attribute);
        log.debug("getAttribute({}, '{}') → '{}'", locator, attribute, value);
        return value;
    }

    public static boolean isDisplayed(By locator) {
        try {
            boolean displayed = DriverManager.get().findElement(locator).isDisplayed();
            log.debug("isDisplayed({}) → {}", locator, displayed);
            return displayed;
        } catch (Exception e) {
            log.debug("isDisplayed({}) → false (element not found: {})", locator, e.getMessage());
            return false;
        }
    }

    public static void scrollIntoView(By locator) {
        log.debug("Scrolling into view: {}", locator);
        WebElement element = WaitUtils.waitForPresence(locator);
        ((JavascriptExecutor) DriverManager.get()).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }
}