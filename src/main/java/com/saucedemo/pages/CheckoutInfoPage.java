package com.saucedemo.pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

@Slf4j
public class CheckoutInfoPage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE_BTN = By.id("continue");

    @Step("Enter first name: {firstName}")
    public CheckoutInfoPage enterFirstName(String firstName) {
        log.debug("Entering first name: {}", firstName);
        type(FIRST_NAME, firstName);
        return this;
    }

    @Step("Enter last name: {lastName}")
    public CheckoutInfoPage enterLastName(String lastName) {
        log.debug("Entering last name: {}", lastName);
        type(LAST_NAME, lastName);
        return this;
    }

    @Step("Enter postal code: {postalCode}")
    public CheckoutInfoPage enterPostalCode(String postalCode) {
        log.debug("Entering postal code: {}", postalCode);
        type(POSTAL_CODE, postalCode);
        return this;
    }

    @Step("Fill checkout info — {firstName} {lastName}, ZIP {postalCode}")
    public CheckoutInfoPage fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        return enterFirstName(firstName).enterLastName(lastName).enterPostalCode(postalCode);
    }

    @Step("Click Continue to proceed to order overview")
    public CheckoutOverviewPage clickContinue() {
        log.info("Clicking Continue on checkout info form");
        click(CONTINUE_BTN);
        return new CheckoutOverviewPage();
    }
}