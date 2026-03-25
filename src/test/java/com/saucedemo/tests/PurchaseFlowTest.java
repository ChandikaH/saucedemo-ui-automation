package com.saucedemo.tests;

import com.saucedemo.core.BaseTest;
import com.saucedemo.core.RetryAnalyzer;
import com.saucedemo.pages.*;
import com.saucedemo.utils.Credentials;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class PurchaseFlowTest extends BaseTest {

    @Epic("SauceDemo")
    @Feature("Purchase Flow")
    @Test(description = "End-to-end: purchase flow for a standard user", retryAnalyzer = RetryAnalyzer.class)
    @Story("Happy path checkout process - standard user")
    @Description("Performs a complete purchase flow: login -> add products -> validate cart -> enter info -> verify totals -> finish order.")
    public void testStandardUserCompletesPurchaseFlow() {

        log.info("Logging in as standard user");
        LoginPage loginPage = new LoginPage();
        ProductsPage productsPage = loginPage.login(Credentials.standardUserUsername(), Credentials.standardUserPassword());
        softAssert.assertEquals(productsPage.getHeaderText(), "Products", "Products page header should display 'Products'");

        log.info("Adding first two products to cart");
        productsPage.addFirstNProducts(2);
        log.info("Validating cart badge count");
        int cartCount = productsPage.getCartCount();
        softAssert.assertEquals(cartCount, 2, "Cart badge should show correct item count");

        log.info("Navigating to cart page");
        CartPage cartPage = productsPage.goToCart();
        log.info("Validating cart contents");
        softAssert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain correct item count");

        log.info("Proceeding to checkout information page");
        CheckoutInfoPage infoPage = cartPage.proceedToCheckout();

        log.info("Filling checkout information");
        CheckoutOverviewPage overviewPage = infoPage.fillCheckoutInformation("John", "Doe", "12345").clickContinue();
        log.info("Validating subtotal, tax, and total amounts");
        overviewPage.validateTotals();
        log.info("Completing checkout");
        CheckoutCompletePage completePage = overviewPage.clickFinish();

        log.info("Validating checkout completion screen");
        softAssert.assertEquals(completePage.getHeaderText(), "Checkout: Complete!", "Checkout complete page header incorrect");
        softAssert.assertEquals(completePage.getConfirmationHeaderText(), "Thank you for your order!", "Success message mismatch");
        softAssert.assertTrue(completePage.isBackToHomeButtonDisplayed(), "Back Home button should be visible");
    }
}
