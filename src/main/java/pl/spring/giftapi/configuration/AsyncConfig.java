package pl.spring.giftapi.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pl.spring.giftapi.properties.AsyncTaskExecutorProperties;

@Configuration
@RequiredArgsConstructor
public class AsyncConfig {

    private final AsyncTaskExecutorProperties taskExecutorProperties;

    @Bean
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        executor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        executor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        executor.setThreadNamePrefix(taskExecutorProperties.getThreadNamePrefix());
        return executor;
    }
}
