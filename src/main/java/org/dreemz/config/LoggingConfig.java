package org.dreemz.config;

import org.dreemz.aspect.LogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingProps.class)
@ConditionalOnProperty(prefix = "http.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingConfig {

    @Bean
    public LogAspect LogAspect(LoggingProps loggingProps){
        return new LogAspect(loggingProps);
    }
}
