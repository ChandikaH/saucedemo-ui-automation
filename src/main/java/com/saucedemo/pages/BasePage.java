package com.saucedemo.pages;

import com.saucedemo.core.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.saucedemo.utils.ElementActions;

@Slf4j
public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.get();
        log.debug("{} page object initialised for thread '{}'", this.getClass().getSimpleName(), Thread.currentThread().getName());
    }

    protected void click(By locator) {
        log.info("Clicking element: {}", locator);
        ElementActions.click(locator);
    }

    protected void type(By locator, String text) {
        log.info("Typing '{}' into element: {}", text, locator);
        ElementActions.type(locator, text);
    }

    protected String getText(By locator) {
        return ElementActions.getText(locator);
    }

    protected boolean isVisible(By locator) {
        return ElementActions.isDisplayed(locator);
    }

    protected void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}