package com.ka.matchinfoservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("com.ka.livescore")
@Data
public class LiveScoreConfiguration {
    private String apiName;
    private String apiUrl;
    private String token;
    private String host;
    private String leagueCode;
    private String timezone;
    private String countryCode;

}
