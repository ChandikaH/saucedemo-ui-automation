package com.saucedemo.pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;


@Slf4j
public class LoginPage extends BasePage {
    private static final By USERNAME_INPUT = By.cssSelector("[data-test='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-test='password']");
    private static final By LOGIN_BUTTON   = By.cssSelector("[data-test='login-button']");

    @Step("Login with username: {username} and password: {password}")
    public ProductsPage login(String username, String password) {
        log.info("Attempting login as '{}'", username);
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        return new ProductsPage();
    }
}
