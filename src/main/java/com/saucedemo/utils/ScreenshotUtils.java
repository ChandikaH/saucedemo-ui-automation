package com.saucedemo.utils;

import com.saucedemo.config.Config;
import com.saucedemo.config.ConfigLoader;
import com.saucedemo.core.DriverManager;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public final class ScreenshotUtils {

    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    private static final Config.Screenshot CFG = ConfigLoader.get().getScreenshot();

    private ScreenshotUtils() {
    }

    public static void captureFailureArtifacts() {
        if (!CFG.isEnabled() || !CFG.isOnFailure()) {
            return;
        }

        WebDriver driver = DriverManager.get();
        captureScreenshotInternal(driver, "failure");
        captureDomInternal(driver, "failure");
    }

    public static void captureScreenshot(String label) {
        if (!CFG.isEnabled()) {
            log.debug("Screenshots disabled in config — skipping");
            return;
        }

        WebDriver driver = DriverManager.get();
        captureScreenshotInternal(driver, label);
    }

    public static void captureDom(String label) {
        WebDriver driver = DriverManager.get();
        captureDomInternal(driver, label);
    }

    private static void captureScreenshotInternal(WebDriver driver, String label) {
        try {
            byte[] pngBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            String filename = buildFilename(label);
            Path dir = Paths.get(CFG.getPath());
            Path screenshotPath = dir.resolve(filename);

            Files.createDirectories(dir);
            Files.write(screenshotPath, pngBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            log.info("Screenshot saved: {}", screenshotPath.toAbsolutePath());

            Allure.addAttachment("Screenshot — " + label, "image/" + CFG.getFormat(), new ByteArrayInputStream(pngBytes), CFG.getFormat());
        } catch (IOException e) {
            log.error("Failed to save screenshot to disk: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage(), e);
        }
    }

    private static void captureDomInternal(WebDriver driver, String label) {
        try {
            String domSnapshot = null;
            if (driver instanceof JavascriptExecutor js) {
                domSnapshot = (String) js.executeScript("return document.documentElement.outerHTML;");
            }
            if (domSnapshot == null || domSnapshot.isBlank()) {
                domSnapshot = driver.getPageSource();
            }

            Allure.addAttachment("DOM Snapshot — " + label, "text/html", new ByteArrayInputStream(domSnapshot.getBytes()), "html");
            log.debug("DOM snapshot attached to Allure for '{}'", label);
        } catch (Exception e) {
            log.warn("Failed to capture DOM snapshot: {}", e.getMessage());
        }
    }

    private static String buildFilename(String label) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
        String safeLabel = label == null ? "unnamed" : label.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        String format = CFG.getFormat();
        return safeLabel + "_" + timestamp + "." + format;
    }
}