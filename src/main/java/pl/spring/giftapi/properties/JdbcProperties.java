package pl.spring.giftapi.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jdbc")
@Getter
@Setter
public class JdbcProperties {

    private int batchSize;
}
