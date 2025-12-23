package com.cvsnewsletter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.lockout")
public class LockoutProperties {
    private int maxAttempts;
    private int durationMinutes;
}
