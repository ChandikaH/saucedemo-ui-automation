package com.saucedemo.utils;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LogUtils {

    private LogUtils() {}

    public static void step(String message, Object... args) {
        String formatted = formatMessage(message, args);
        log.info("[STEP] {}", formatted);
        Allure.step(formatted);
    }

    public static void info(String message, Object... args) {
        log.info(formatMessage(message, args));
    }

    public static void debug(String message, Object... args) {
        log.debug(formatMessage(message, args));
    }

    public static void warn(String message, Object... args) {
        String formatted = formatMessage(message, args);
        log.warn("[WARN] {}", formatted);
        Allure.addAttachment("Warning", formatted);
    }

    public static void error(String message, Object... args) {
        log.error(formatMessage(message, args));
    }

    private static String formatMessage(String template, Object[] args) {
        if (template == null) {
            return "";
        }
        if (args == null || args.length == 0) {
            return template;
        }
        String result = template;
        for (Object arg : args) {
            int idx = result.indexOf("{}");
            if (idx < 0) break;
            result = result.substring(0, idx)
                    + (arg == null ? "null" : arg)
                    + result.substring(idx + 2);
        }
        return result;
    }
}