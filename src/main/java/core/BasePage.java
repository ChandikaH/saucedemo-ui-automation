package core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ElementActions;
import utils.WaitUtils;

@Slf4j
@Getter
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils wait;
    protected final ElementActions actions;

    protected BasePage(TestContext context) {
        this.driver = context.driver();
        this.wait = new WaitUtils(driver);
        this.actions = new ElementActions(driver);
        log.debug("{} page object initialised for thread '{}'",
                this.getClass().getSimpleName(),
                Thread.currentThread().getName());
    }

    protected void click(By locator) {
        log.info("Clicking element: {}", locator);
        wait.waitForClickable(locator);
        actions.click(locator);
    }

    protected void type(By locator, String text) {
        log.info("Typing '{}' into element: {}", text, locator);
        wait.waitForVisible(locator);
        actions.type(locator, text);
    }

    protected String getText(By locator) {
        wait.waitForVisible(locator);
        return actions.getText(locator);
    }

    protected boolean isVisible(By locator) {
        return wait.isVisible(locator);
    }

    protected void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public abstract boolean isLoaded();
}