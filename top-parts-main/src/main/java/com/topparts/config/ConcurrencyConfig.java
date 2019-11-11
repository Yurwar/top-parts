package com.topparts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ConcurrencyConfig {
    @Bean
    public ThreadPoolTaskExecutor executor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        taskExecutor.setCorePoolSize(availableProcessors);
        taskExecutor.setMaxPoolSize(availableProcessors * 2);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("TaskExecutor-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
