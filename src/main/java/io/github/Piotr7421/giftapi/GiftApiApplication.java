package io.github.Piotr7421.giftapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import io.github.Piotr7421.giftapi.properties.AsyncTaskExecutorProperties;
import io.github.Piotr7421.giftapi.properties.JdbcProperties;

@EnableAsync
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties({JdbcProperties.class, AsyncTaskExecutorProperties.class})
public class GiftApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiftApiApplication.class, args);
    }
}
