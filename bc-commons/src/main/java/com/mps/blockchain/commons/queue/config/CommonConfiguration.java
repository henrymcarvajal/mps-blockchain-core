package com.mps.blockchain.commons.queue.config;

import static java.lang.System.getenv;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    
    public static String getEnvOrThrow(String name) {
        final String env = getenv(name);
        if (env == null) {
            throw new IllegalStateException("Environment variable [" + name + "] is not set.");
        }
        return env;
    }
}
