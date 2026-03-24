package config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Represents the full configuration model loaded from config.json.
 * This model holds all framework settings including browser, environment,
 * timeouts, screenshots, Allure, retry logic, and parallel execution options.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    private Environment environment;
    private Browser browser;
    private Timeouts timeouts;
    private Screenshot screenshot;
    private Allure allure;
    private Retry retry;
    private Execution execution;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Environment {
        private String name;
        private String baseUrl;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Browser {
        private String type;
        private boolean headless;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Timeouts {
        private int explicit;
        private int pageLoad;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Screenshot {
        private boolean enabled;
        private boolean onFailure;
        private String format;
        private String path;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Allure {
        private boolean enabled;
        private String resultsPath;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Retry {
        private boolean enabled;
        private int maxAttempts;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Execution {
        private boolean parallel;
        private int threadCount;
    }
}
