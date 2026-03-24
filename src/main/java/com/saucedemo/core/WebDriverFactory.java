package com.saucedemo.core;

import com.saucedemo.config.Config;
import com.saucedemo.config.ConfigLoader;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * WebDriver Factory for creating browser instances.
 * Supports Chrome, Firefox, and Edge with configurable options.
 */
@Slf4j
public final class WebDriverFactory {

    private static final Config CONFIG = ConfigLoader.get();

    private WebDriverFactory() {
        // Utility class constructor to prevent instantiation
        throw new UnsupportedOperationException("Utility class");
    }

    public static WebDriver create() {
        String browserType = CONFIG.getBrowser().getType();
        if (browserType == null || browserType.isBlank()) {
            throw new IllegalArgumentException("Browser type from configuration cannot be null or empty");
        }
        browserType = browserType.toLowerCase().trim();
        boolean headless = CONFIG.getBrowser().isHeadless();

        log.info("Creating '{}' WebDriver (headless={})", browserType, headless);

        WebDriver driver = switch (browserType) {
            case "chrome" -> createChrome(headless);
            case "firefox" -> createFirefox(headless);
            case "edge" -> createEdge(headless);
            default -> throw new IllegalArgumentException("Unsupported browser type: '" + browserType + "'. Supported: chrome, firefox, edge");
        };

        applyTimeoutsAndWindow(driver);
        log.debug("WebDriver created successfully for browser '{}'", browserType);
        return driver;
    }

    private static WebDriver createChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--disable-extensions",
                "--disable-infobars",
                "--remote-allow-origins=*"
        );
        options.setAcceptInsecureCerts(true);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        log.debug("ChromeOptions configured");
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--width=1920", "--height=1080");
        options.setAcceptInsecureCerts(true);
        log.debug("FirefoxOptions configured");
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdge(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080"
        );
        options.setAcceptInsecureCerts(true);
        log.debug("EdgeOptions configured");
        return new EdgeDriver(options);
    }

    private static void applyTimeoutsAndWindow(WebDriver driver) {
        int pageLoad = CONFIG.getTimeouts().getPageLoad();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
        driver.manage().window().maximize();
        log.debug("Applied page-load timeout: {}s and maximized window", pageLoad);
    }
}