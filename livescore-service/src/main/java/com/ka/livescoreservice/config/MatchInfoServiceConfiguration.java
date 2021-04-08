package com.ka.livescoreservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("com.ka.livescore.matchids")
@Data
public class MatchInfoServiceConfiguration {
    private String apiName;
    private String apiUrl;
}
