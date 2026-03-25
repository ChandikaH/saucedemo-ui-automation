package com.saucedemo.core;

import com.saucedemo.config.ConfigLoader;
import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
    private int attemptCount = 0;
    private static final int MAX_ATTEMPTS;
    private static final boolean RETRY_ENABLED;

    static {
        var retryCfg = ConfigLoader.get().getRetry();
        RETRY_ENABLED = retryCfg.isEnabled();
        MAX_ATTEMPTS = RETRY_ENABLED ? retryCfg.getMaxAttempts() : 0;
    }

    @Override
    public boolean retry(ITestResult result) {
        if (!RETRY_ENABLED) {
            return false;
        }
        if (attemptCount < MAX_ATTEMPTS) {
            attemptCount++;
            log.warn("Test '{}' failed — retrying (attempt {}/{})", result.getName(), attemptCount, MAX_ATTEMPTS);
            return true;
        }
        log.error("Test '{}' failed after {} attempt(s) — marking as FAILED", result.getName(), attemptCount + 1);
        return false;
    }
}