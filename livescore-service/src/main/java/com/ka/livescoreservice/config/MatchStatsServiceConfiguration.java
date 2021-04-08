package com.ka.livescoreservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("com.ka.livescore.matchstats")
public class MatchStatsServiceConfiguration {
    private String apiName;
    private String apiUrl;
}
