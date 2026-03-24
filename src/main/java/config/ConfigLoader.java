package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * Loads the framework configuration from config.json using Jackson.
 * Ensures the configuration is immutable and loaded once at startup.
 */
@Slf4j
public final class ConfigLoader {

    private static final String DEFAULT_CONFIG_FILE = "config.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Config INSTANCE;

    static {
        INSTANCE = load();
    }

    private ConfigLoader() {
    }

    public static Config get() {
        return INSTANCE;
    }

    private static Config load() {
        String env = System.getProperty("env");
        String configFile = (env != null && !env.isBlank()) ? "config-%s.json".formatted(env) : DEFAULT_CONFIG_FILE;

        log.info("Loading configuration. env='{}', file='{}'", env, configFile);
        InputStream stream = ConfigLoader.class.getClassLoader().getResourceAsStream(configFile);

        if (stream == null && !DEFAULT_CONFIG_FILE.equals(configFile)) {
            log.warn("Config '{}' not found. Falling back to '{}'", configFile, DEFAULT_CONFIG_FILE);
            stream = ConfigLoader.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE);
        }

        if (stream == null) {
            throw new IllegalStateException("No configuration file found. Checked: %s and %s".formatted(configFile, DEFAULT_CONFIG_FILE));
        }

        try (InputStream inputStream = stream) {
            Config config = MAPPER.readValue(inputStream, Config.class);
            if (env != null && !env.isBlank()) {
                config.getEnvironment().setName(env);
            }
            log.info("Configuration loaded — environment='{}', browser='{}', headless={}", config.getEnvironment().getName(), config.getBrowser().getType(), config.getBrowser().isHeadless());
            return config;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse configuration file: " + configFile, e);
        }
    }
}