package core;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public final class DriverFactory {

    private DriverFactory() {}

    public static WebDriver initDriver() {
        WebDriver driver = WebDriverFactory.create();
        DriverManager.set(driver);
        log.info("WebDriver initialised and stored for thread '{}'",
                Thread.currentThread().getName());
        return driver;
    }

    public static void tearDownDriver() {
        log.info("Tearing down WebDriver for thread '{}'",
                Thread.currentThread().getName());
        DriverManager.quit();
    }
}