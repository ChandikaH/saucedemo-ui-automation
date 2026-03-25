package com.saucedemo.utils;

import com.saucedemo.config.ConfigLoader;

public final class Credentials {

    private Credentials() {
    }

    public static String standardUserUsername() {
        String fromEnv = System.getenv("STD_USER_USERNAME");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        return ConfigLoader.get().getTestdata().getUsername();
    }

    public static String standardUserPassword() {
        String fromEnv = System.getenv("STD_USER_PASSWORD");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        return ConfigLoader.get().getTestdata().getPassword();
    }
}