package com.ka.matchstatsservice.integrators;

import com.ka.matchstatsservice.clients.LivescoreApiClient;
import com.ka.matchstatsservice.config.LiveScoreConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@CommonsLog
public class LiveScoreIntegrator {
    private final LivescoreApiClient livescoreApiClient;

    public String getMatchStats(String matchID, LiveScoreConfiguration liveScoreConfiguration){
        log.info("Attempting to call LiveScore API");
        try {
            String result = livescoreApiClient.getMatchStats(
                    liveScoreConfiguration.getToken(), liveScoreConfiguration.getHost(),
                    matchID);
            log.info("LiveScore API called successfully");
            return result;
        } catch (Exception e){
            log.error("Call to LiveScore API failed. Error : " + e);
            return null; //TODO: Throw custom exception here
        }
    }
}
