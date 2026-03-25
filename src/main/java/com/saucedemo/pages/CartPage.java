package com.saucedemo.pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@Slf4j
public class CartPage extends BasePage {

    private static final By CART_ITEM = By.cssSelector(".cart_item");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    @Step("Get cart item count")
    public int getCartItemCount() {
        List<WebElement> items = driver.findElements(CART_ITEM);
        log.debug("Cart contains {} item(s)", items.size());
        return items.size();
    }

    @Step("Click Checkout button")
    public CheckoutInfoPage proceedToCheckout() {
        log.info("Proceeding to checkout from cart");
        click(CHECKOUT_BUTTON);
        return new CheckoutInfoPage();
    }
}