package core;

import config.Config;
import config.ConfigLoader;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

@Slf4j
public final class WebDriverFactory {

    private static final Config CONFIG = ConfigLoader.get();

    private WebDriverFactory() {
    }

    public static WebDriver create() {
        String browserType = CONFIG.getBrowser().getType().toLowerCase().trim();
        boolean headless = CONFIG.getBrowser().isHeadless();

        log.info("Creating '{}' WebDriver (headless={})", browserType, headless);

        WebDriver driver = switch (browserType) {
            case "chrome" -> createChrome(headless);
            case "firefox" -> createFirefox(headless);
            case "edge" -> createEdge(headless);
            default -> throw new IllegalArgumentException("Unsupported browser type: '" + browserType + "'");
        };

        applyTimeoutsAndWindow(driver);
        return driver;
    }

    private static WebDriver createChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1920,1080", "--disable-extensions", "--disable-infobars", "--remote-allow-origins=*");
        options.setAcceptInsecureCerts(true);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        log.debug("ChromeOptions: {}", options.asMap());
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
}