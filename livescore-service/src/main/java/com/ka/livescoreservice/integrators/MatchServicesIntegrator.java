package com.ka.livescoreservice.integrators;

import com.ka.livescoreservice.config.MatchInfoServiceConfiguration;
import com.ka.livescoreservice.config.MatchLineupsServiceConfiguration;
import com.ka.livescoreservice.config.MatchStatsServiceConfiguration;
import com.ka.livescoreservice.models.MatchWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@AllArgsConstructor
@CommonsLog
public class MatchServicesIntegrator {

    private static final String ATTEMPT_CALL = "Attempting to call ";
    private static final String FAILED_CALL = "Failed to call ";
    private static final String ERROR = ". Error: ";
    private static final String SUCCESSFUL_CALL = " called successfully";
    private static final String MATCH_INFO_SERVICE = "match-info-service";
    private static final String MATCH_LINEUPS_SERVICE = "match-lineups-service";
    private static final String MATCH_STATS_SERVICE = "match-stats-service";

    @Autowired
    private RestTemplate restTemplate;

    private final MatchInfoServiceConfiguration matchInfoServiceConfiguration;
    private final MatchStatsServiceConfiguration matchStatsServiceConfiguration;
    private final MatchLineupsServiceConfiguration matchLineupsServiceConfiguration;

    public String getMatchIDs(String teamName){
        log.info(ATTEMPT_CALL + MATCH_INFO_SERVICE);
        try {
            String response = restTemplate.getForObject(matchInfoServiceConfiguration.getApiUrl() +
                    teamName, String.class);
            log.info(MATCH_INFO_SERVICE + SUCCESSFUL_CALL);
            return response;
        } catch (Exception e) {
            log.error(FAILED_CALL + MATCH_INFO_SERVICE + ERROR + e);
            return null; //TODO: throw custom exception




        }
    }

    public String getMatchStats(MatchWrapper matchID) {
        log.info(ATTEMPT_CALL + MATCH_STATS_SERVICE);
        try {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(new JSONObject(matchID).toString(), httpHeaders);
        String result = restTemplate.postForObject(matchStatsServiceConfiguration.getApiUrl(), request, String.class);
        log.info(MATCH_STATS_SERVICE + SUCCESSFUL_CALL);
        return result;
        } catch (Exception e) {
            log.error(FAILED_CALL + MATCH_STATS_SERVICE + ERROR + e);
            return null; //TODO: throw custom exception
        }
    }

    public String getMatchLineups(MatchWrapper matchID) {
        log.info(ATTEMPT_CALL + MATCH_LINEUPS_SERVICE);
        try {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(new JSONObject(matchID).toString(), httpHeaders);
        String result = restTemplate.postForObject(matchLineupsServiceConfiguration.getApiUrl(), request, String.class);
        log.info(MATCH_LINEUPS_SERVICE + SUCCESSFUL_CALL);
        return result;
        } catch (Exception e) {
            log.error(FAILED_CALL + MATCH_STATS_SERVICE + ERROR + e);
            return null; //TODO: throw custom exception
        }
    }

}
