package com.huazi.huaziclientsdk;

import com.huazi.huaziclientsdk.client.YuApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ComponentScan
@ConfigurationProperties("huazi.client")
public class huaziClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public YuApiClient yuApiClient(){
        return new YuApiClient(accessKey,secretKey);
    }
}
