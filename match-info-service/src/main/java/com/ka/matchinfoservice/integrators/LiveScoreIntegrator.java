package com.ka.matchinfoservice.integrators;

import com.ka.matchinfoservice.clients.LiveScoreApiClient;
import com.ka.matchinfoservice.config.LiveScoreConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@CommonsLog
public class LiveScoreIntegrator {

    private final LiveScoreApiClient liveScoreApiClient;

    public String getLatestMatch(String date, LiveScoreConfiguration liveScoreConfiguration){
        log.info("Attempting to call LiveScore API");
        try {
            String result = liveScoreApiClient.getMatchesByDate(liveScoreConfiguration.getToken(),
                    liveScoreConfiguration.getHost(), date,
                    liveScoreConfiguration.getLeagueCode(), liveScoreConfiguration.getTimezone(),
                    liveScoreConfiguration.getCountryCode());
            log.info("LiveScore API called successfully");
            return result;
        } catch (Exception e) {
            log.error("Call to LiveScore API failed. Error : " + e);
            return null; //TODO: Throw custom exception here
        }
    }

}
