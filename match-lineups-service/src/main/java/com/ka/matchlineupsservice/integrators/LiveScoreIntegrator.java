package com.ka.matchlineupsservice.integrators;

import com.ka.matchlineupsservice.clients.LivescoreApiClient;
import com.ka.matchlineupsservice.config.LiveScoreConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@CommonsLog
public class LiveScoreIntegrator {
    private final LivescoreApiClient livescoreApiClient;

    public String getLineups(String matchID, LiveScoreConfiguration liveScoreConfiguration){
        log.info("Attempting to call LiveScore API to retrieve match lineups. MatchID: " + matchID);
        try {
            String result = livescoreApiClient.getMatchLineups(liveScoreConfiguration.getToken(),
                    liveScoreConfiguration.getHost(), matchID);
            log.info("LiveScore API called successfully");
            return result;
        } catch (Exception e) {
            log.error("Call to LiveScore API failed. Error : " + e);
            return null; //TODO: Throw custom exception here
        }
    }
}
