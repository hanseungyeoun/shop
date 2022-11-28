package com.example.shop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("download")
public class DownloadProperties {
    private final String defaultPath;

    public DownloadProperties(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    public String getDefaultPath() {
        return defaultPath;
    }
}
