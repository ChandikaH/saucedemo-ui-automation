package com.saucedemo.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

@Slf4j
public class ProductsPage extends BasePage {

    private static final By PAGE_HEADER         = By.cssSelector(".title");
    private static final By INVENTORY_CONTAINER = By.id("inventory_container");
    private static final By INVENTORY_ITEMS     = By.cssSelector(".inventory_item");
    private static final By CART_LINK           = By.cssSelector(".shopping_cart_link");
    private static final By CART_BADGE          = By.cssSelector(".shopping_cart_badge");
    private static final By SORT_DROPDOWN       = By.cssSelector(".product_sort_container");

    public String getHeaderTitle() {
        return getText(PAGE_HEADER);
    }
}
