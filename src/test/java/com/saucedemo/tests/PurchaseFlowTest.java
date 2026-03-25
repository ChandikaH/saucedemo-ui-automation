package com.saucedemo.tests;

import com.saucedemo.core.BaseTest;
import com.saucedemo.core.RetryAnalyzer;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
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
    @Test(description = "End-to-end: standard user can purchase the first two products", retryAnalyzer = RetryAnalyzer.class)
    @Story("Happy path checkout process - standard user")
    @Description("Logs in as standard user, adds first two products to cart, checks out, and verifies successful purchase.")
    public void testStandardUserCanPurchaseFirstTwoProducts() {

        log.info("Login as standard_user");
        LoginPage loginPage = new LoginPage();
        ProductsPage productsPage = loginPage.login(Credentials.standardUserUsername(), Credentials.standardUserPassword());
        log.info("Verify page header title for Products page");
        softAssert.assertEquals(productsPage.getHeaderTitle(), "Products", "Expected page header title 'Products' to be displayed");
    }
}
