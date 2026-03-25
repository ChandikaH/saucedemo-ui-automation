package com.saucedemo.pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

@Slf4j
public class CheckoutCompletePage extends BasePage {

    private static final By PAGE_HEADER = By.cssSelector(".title");
    private static final By CONFIRMATION_HEADER = By.cssSelector(".complete-header");
    private static final By BACK_HOME_BUTTON = By.id("back-to-products");

    @Step("Read order confirmation header")
    public String getConfirmationHeaderText() {
        String header = getText(CONFIRMATION_HEADER).trim();
        log.info("Confirmation header: '{}'", header);
        return header;
    }

    public boolean isBackToHomeButtonDisplayed() {
        return isVisible(BACK_HOME_BUTTON);
    }

    public String getHeaderText() {
        return getText(PAGE_HEADER);
    }
}