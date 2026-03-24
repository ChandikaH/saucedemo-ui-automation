package com.saucedemo.core;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.UUID;

@Slf4j
public record TestContext(WebDriver driver, String correlationId) {

    public TestContext {
        if (driver == null) {
            throw new IllegalArgumentException("TestContext cannot be created with a null driver");
        }
        if (correlationId == null || correlationId.isBlank()) {
            throw new IllegalArgumentException("TestContext cannot be created with a null or blank correlationId");
        }
        log.debug("TestContext created with correlationId='{}' for thread '{}'", correlationId, Thread.currentThread().getName());
    }

    public static TestContext fromCurrentThread() {
        return new TestContext(DriverManager.get(), UUID.randomUUID().toString());
    }
}