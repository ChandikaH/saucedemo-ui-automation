package com.saucedemo.utils;

import com.saucedemo.core.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;

@Slf4j
public final class WebDriverExtensions {

    private WebDriverExtensions() {
    }

    public static boolean isPageReady() {
        Object state = js().executeScript("return document.readyState");
        boolean ready = "complete".equals(state);
        log.debug("document.readyState = '{}' → ready={}", state, ready);
        return ready;
    }

    public static String getPageTitle() {
        return (String) js().executeScript("return document.title;");
    }

    public static void setValue(WebElement element, String value) {
        log.debug("JS setValue on element to '{}'", value);
        js().executeScript("arguments[0].value = arguments[1];", element, value);
    }

    public static void jsSetValue(By locator, String value) {
        WebElement element = find(locator);
        if (element == null) return;
        setValue(element, value);
    }

    public static void click(WebElement element) {
        log.debug("JS click on element");
        js().executeScript("arguments[0].click();", element);
    }

    public static void jsClick(By locator) {
        WebElement element = find(locator);
        if (element == null) return;
        click(element);
    }

    public static void scrollIntoView(WebElement element) {
        log.debug("Scrolling element into view");
        js().executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center',inline:'nearest'});", element);
    }

    public static void scrollToElement(By locator) {
        WebElement element = find(locator);
        if (element == null) return;
        scrollIntoView(element);
    }

    public static void scrollAndClick(By locator) {
        scrollToElement(locator);
        jsClick(locator);
    }

    public static void highlight(WebElement element) {
        log.debug("Highlighting element (debug)");
        JavascriptExecutor executor = js();
        String original = (String) executor.executeScript("return arguments[0].style.border;", element);
        executor.executeScript("arguments[0].style.border='3px solid red';", element);
        executor.executeScript("window.setTimeout(function(el, orig){ el.style.border = orig; }, 500, arguments[0], arguments[1]);", element, original);
    }

    public static void highlight(By locator) {
        WebElement element = find(locator);
        if (element == null) return;
        highlight(element);
    }

    public static String getAttributeJS(By locator, String attribute) {
        WebElement element = find(locator);
        if (element == null) return null;
        try {
            return (String) js().executeScript("return arguments[0].getAttribute(arguments[1]);", element, attribute);
        } catch (Exception ex) {
            log.error("Failed getting JS attribute '{}' on {} : {}", attribute, locator, ex.getMessage());
            return null;
        }
    }

    public static void focus(By locator) {
        WebElement element = find(locator);
        if (element == null) return;
        try {
            js().executeScript("arguments[0].focus();", element);
        } catch (Exception ex) {
            log.error("Failed to focus element {} : {}", locator, ex.getMessage());
        }
    }

    public static void scrollToTop() {
        js().executeScript("window.scrollTo({ top: 0, behavior: 'smooth' });");
    }

    public static void scrollToBottom() {
        js().executeScript("window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });");
    }

    private static WebElement find(By locator) {
        try {
            return DriverManager.get().findElement(locator);
        } catch (NoSuchElementException e) {
            log.error("Element not found: {}", locator);
        } catch (Exception e) {
            log.error("Error finding element {} : {}", locator, e.getMessage());
        }
        return null;
    }

    private static JavascriptExecutor js() {
        WebDriver driver = DriverManager.get();
        if (!(driver instanceof JavascriptExecutor executor)) {
            throw new UnsupportedOperationException("Current WebDriver does not support JavaScript execution: " + driver.getClass().getSimpleName());
        }
        return executor;
    }
}