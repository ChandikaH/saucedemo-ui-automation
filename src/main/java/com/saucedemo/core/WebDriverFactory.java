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
import java.util.Map;

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
        String browserType = resolveBrowserType();
        boolean headless = CONFIG.getBrowser().isHeadless();

        log.info("Creating '{}' WebDriver (headless={})", browserType, headless);

        WebDriver driver = switch (browserType) {
            case "chrome" -> createChrome(headless);
            case "firefox" -> createFirefox(headless);
            case "edge" -> createEdge(headless);
            default ->
                    throw new IllegalArgumentException("Unsupported browser type: '" + browserType + "'. Supported: chrome, firefox, edge");
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
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1920,1080", "--incognito", "--disable-extensions", "--disable-notifications", "--disable-infobars", "--disable-popup-blocking", "--disable-sync");
        options.addArguments("--disable-features=PasswordManagerUI,PasswordManagerEnabled,PasswordReuseDetection,PasswordLeakDetection,SafetyCheck");
        options.setExperimentalOption("prefs", Map.of("credentials_enable_service", false, "profile.password_manager_enabled", false, "profile.password_manager_leak_detection", false, "profile.autofill_profile_enabled", false, "autofill.credit_card_enabled", false));
        options.setAcceptInsecureCerts(true);
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
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1920,1080");
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

    private static String resolveBrowserType() {
        String fromEnv = System.getenv("BROWSER");
        String fromProp = System.getProperty("browser");
        String fromConfig = CONFIG.getBrowser().getType();

        String browserType;
        if (fromEnv != null && !fromEnv.isBlank()) {
            browserType = fromEnv;
        } else if (fromProp != null && !fromProp.isBlank()) {
            browserType = fromProp;
        } else {
            browserType = fromConfig;
        }

        if (browserType == null || browserType.isBlank()) {
            throw new IllegalArgumentException("Browser type cannot be null or empty");
        }
        return browserType.toLowerCase().trim();
    }
}