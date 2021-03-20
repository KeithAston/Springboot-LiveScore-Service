package com.ka.matchstatsservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("com.ka.livescore")
public class LiveScoreConfiguration {
    private String apiName;
    private String apiUrl;
    private String token;
    private String host;
}
