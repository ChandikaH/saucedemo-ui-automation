package core;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public record TestContext(WebDriver driver) {

    public TestContext {
        if (driver == null) {
            throw new IllegalArgumentException("TestContext cannot be created with a null driver");
        }
        log.debug("TestContext created for thread '{}'", Thread.currentThread().getName());
    }

    public static TestContext fromCurrentThread() {
        return new TestContext(DriverManager.get());
    }
}