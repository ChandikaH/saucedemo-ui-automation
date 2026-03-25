package com.saucedemo.core;

import com.saucedemo.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        log.info("==== Test Suite START: {} ====", suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("==== Test Suite END: {} ====", suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("---- TEST STARTED  — {}.{}", result.getTestClass().getName(), result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("---- TEST PASSED   — {} ({}ms)", result.getName(), elapsedMs(result));
        Allure.label("status", "passed");
        ScreenshotUtils.captureScreenshot("passed_" + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("---- TEST FAILED   — {} ({}ms)", result.getName(), elapsedMs(result));

        Throwable cause = result.getThrowable();
        if (cause != null) {
            log.error("Failure reason: {}", cause.getMessage());
        }

        captureFailureArtifacts(result);
        Allure.label("status", "failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("---- TEST SKIPPED  — {}", result.getName());
        Allure.label("status", "skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("---- TEST FAILED (within success %) — {}", result.getName());
    }

    private void captureFailureArtifacts(ITestResult result) {
        try {
            String label = result.getName();
            log.debug("Capturing failure artifacts for: {}", label);
            ScreenshotUtils.captureScreenshot(label);
            ScreenshotUtils.captureDom(label);
        } catch (IllegalStateException noDriver) {
            log.warn("Could not capture failure artifacts — no driver available: {}", noDriver.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error while capturing failure artifacts: {}", e.getMessage());
        }
    }

    private String elapsedMs(ITestResult result) {
        return String.valueOf(result.getEndMillis() - result.getStartMillis());
    }
}