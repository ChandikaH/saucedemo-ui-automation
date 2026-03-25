package com.saucedemo.pages;

import com.saucedemo.components.ProductCardComponent;
import com.saucedemo.utils.WaitUtils;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class ProductsPage extends BasePage {

    private static final By PAGE_HEADER = By.cssSelector(".title");
    private static final By INVENTORY_CONTAINER = By.id("inventory_container");
    private static final By INVENTORY_ITEMS = By.cssSelector(".inventory_item");
    private static final By CART_LINK = By.cssSelector(".shopping_cart_link");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");

    public String getHeaderText() {
        return getText(PAGE_HEADER);
    }

    @Step("Get all product cards")
    public List<ProductCardComponent> getProductCards() {
        WaitUtils.waitForVisibility(INVENTORY_CONTAINER);
        List<WebElement> items = driver.findElements(INVENTORY_ITEMS);
        log.info("Found {} product cards on the page", items.size());
        return items.stream().map(ProductCardComponent::new).toList();
    }

    @Step("Add first {count} products to cart")
    public void addFirstNProducts(int count) {
        List<ProductCardComponent> items = getProductCards();
        if (count > items.size()) {
            throw new IllegalArgumentException("Requested to add " + count + " products but only " + items.size() + " available");
        }
        IntStream.range(0, count).forEach(i -> items.get(i).addToCart());
    }

    @Step("Navigate to shopping cart")
    public CartPage goToCart() {
        log.info("Navigating to cart");
        click(CART_LINK);
        return new CartPage();
    }

    @Step("Read cart item count from badge")
    public int getCartCount() {
        if (!isVisible(CART_BADGE)) {
            log.debug("Cart badge not visible — returning 0");
            return 0;
        }
        int count = Integer.parseInt(getText(CART_BADGE).trim());
        log.debug("Cart badge count: {}", count);
        return count;
    }
}