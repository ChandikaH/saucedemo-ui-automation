package com.saucedemo.core;

import com.saucedemo.config.ConfigLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

@Slf4j
@Listeners({TestListener.class, io.qameta.allure.testng.AllureTestNg.class})
public abstract class BaseTest {

    @Getter
    protected TestContext context;

    @Getter
    protected SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        String testName = method.getName();
        log.info("==========================================");
        log.info("Starting test: {}", testName);
        log.info("Thread: {}", Thread.currentThread().getName());
        log.info("==========================================");

        DriverFactory.initDriver();
        context = TestContext.fromCurrentThread();
        softAssert = new SoftAssert();

        var cfg = ConfigLoader.get();
        context.driver().get(cfg.getEnvironment().getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (softAssert != null) {
                softAssert.assertAll();
            }
        } catch (AssertionError softFailure) {
            log.error("Soft assertion failure(s) detected after test '{}': {}", result.getName(), softFailure.getMessage());
            throw softFailure;
        } finally {
            log.info("Tearing down after test: {} — status: {}", result.getName(), getStatusLabel(result.getStatus()));
            try {
                DriverFactory.tearDownDriver();
            } catch (Exception e) {
                log.warn("Exception during driver teardown for test '{}': {}", result.getName(), e.getMessage(), e);
            }
        }
    }

    protected void assertAll() {
        softAssert.assertAll();
    }

    private String getStatusLabel(int status) {
        return switch (status) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN(" + status + ")";
        };
    }
}

