package com.saucedemo.components;

import com.saucedemo.utils.WaitUtils;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Slf4j
public record ProductCardComponent(WebElement root) {

    private static final By NAME_LOCATOR = By.cssSelector(".inventory_item_name");
    private static final By DESCRIPTION_LOCATOR = By.cssSelector(".inventory_item_desc");
    private static final By PRICE_LOCATOR = By.cssSelector(".inventory_item_price");
    private static final By BUTTON_LOCATOR = By.cssSelector("button.btn_inventory");

    public String getName() {
        return root.findElement(NAME_LOCATOR).getText().trim();
    }

    public String getDescription() {
        return root.findElement(DESCRIPTION_LOCATOR).getText().trim();
    }

    public String getPrice() {
        return root.findElement(PRICE_LOCATOR).getText().trim();
    }

    private WebElement getButton() {
        return root.findElement(BUTTON_LOCATOR);
    }

    public String getCartButtonText() {
        return getButton().getText().trim();
    }

    public boolean isInCart() {
        return "Remove".equalsIgnoreCase(getCartButtonText());
    }

    public boolean isAddable() {
        return "Add to cart".equalsIgnoreCase(getCartButtonText());
    }

    @Step("Add product '{name}' to cart")
    public void addToCart() {
        String name = getName();
        log.info("Attempting to add product to cart: '{}'", name);
        if (!isAddable()) {
            log.warn("Product '{}' is already in cart — skipping add.", name);
            return;
        }
        WaitUtils.waitForClickability(getButton()).click();
        log.info("Product '{}' added to cart.", name);
    }
}