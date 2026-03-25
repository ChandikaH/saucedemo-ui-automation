package com.saucedemo.pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.List;

@Slf4j
public class CheckoutOverviewPage extends BasePage {

    private static final By CART_ITEMS = By.cssSelector(".cart_item");
    private static final By ITEM_PRICE = By.cssSelector(".inventory_item_price");
    private static final By SUBTOTAL_LABEL = By.cssSelector(".summary_subtotal_label");
    private static final By TAX_LABEL = By.cssSelector(".summary_tax_label");
    private static final By TOTAL_LABEL = By.cssSelector(".summary_total_label");
    private static final By FINISH_BUTTON = By.id("finish");

    @Step("Get item prices from order summary")
    public List<Double> getItemPrices() {
        return driver.findElements(CART_ITEMS).stream().map(item -> item.findElement(ITEM_PRICE).getText().replace("$", "").trim()).map(Double::parseDouble).toList();
    }

    @Step("Calculate subtotal based on item prices")
    public double calculateSubtotal() {
        return getItemPrices().stream().mapToDouble(Double::doubleValue).sum();
    }

    @Step("Get displayed subtotal from UI")
    public double getDisplayedSubtotal() {
        String raw = getText(SUBTOTAL_LABEL).replace("Item total: $", "").trim();
        return Double.parseDouble(raw);
    }

    @Step("Get displayed tax from UI")
    public double getDisplayedTax() {
        String raw = getText(TAX_LABEL).replace("Tax: $", "").trim();
        return Double.parseDouble(raw);
    }

    @Step("Get displayed total from UI")
    public double getDisplayedTotal() {
        String raw = getText(TOTAL_LABEL).replace("Total: $", "").trim();
        return Double.parseDouble(raw);
    }

    @Step("Click Finish to complete the order")
    public CheckoutCompletePage clickFinish() {
        log.info("Clicking Finish to place order");
        click(FINISH_BUTTON);
        return new CheckoutCompletePage();
    }

    @Step("Validate subtotal, tax, and total values")
    public void validateTotals() {

        double calculatedSubtotal = calculateSubtotal();
        double displayedSubtotal = getDisplayedSubtotal();
        double displayedTax = getDisplayedTax();
        double displayedTotal = getDisplayedTotal();
        double expectedTotal = calculatedSubtotal + displayedTax;

        log.info("Calculated subtotal: {}", calculatedSubtotal);
        log.info("Displayed subtotal: {}", displayedSubtotal);
        log.info("Displayed tax: {}", displayedTax);
        log.info("Expected total: {}", expectedTotal);
        log.info("Displayed total: {}", displayedTotal);

        Assert.assertEquals(displayedSubtotal, calculatedSubtotal, 0.01, "Displayed subtotal does not match calculated subtotal");
        Assert.assertEquals(displayedTotal, expectedTotal, 0.01, "Displayed total does not match expected total");
    }
}