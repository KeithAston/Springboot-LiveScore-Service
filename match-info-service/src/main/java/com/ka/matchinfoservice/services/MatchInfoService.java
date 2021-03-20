package com.ka.matchinfoservice.services;

import com.ka.matchinfoservice.config.LiveScoreConfiguration;
import com.ka.matchinfoservice.integrators.LiveScoreIntegrator;
import com.ka.matchinfoservice.models.Match;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
@CommonsLog
public class MatchInfoService {

    private final LiveScoreIntegrator liveScoreIntegrator;
    private final LiveScoreConfiguration liveScoreConfiguration;
    private static final String LAST_GAME = "last";
    private static final String NEXT_GAME = "next";
    private static final String CURRENT_GAME = "current";
    private static final String FULL_TIME = "FT";
    private static final String NOT_STARTED = "NS";

    private Map<String,String> matchIds;

    public Map<String,String> getMatchIDs(String teamName) {
        matchIds = new HashMap<>();
        String jsonResponse;
        Match match;
        try {
            jsonResponse = liveScoreIntegrator.getLatestMatch(getCurrDate(), liveScoreConfiguration);
            match = getMatchByTeamName(jsonResponse, teamName);
        } catch (Exception e) {
            log.error("Integrator failed to call LiveScore API" + e);
            match = null;
        }

        if (match != null) {
            if (match.getStatus().equalsIgnoreCase(FULL_TIME)) {
                matchIds.put(LAST_GAME, match.getMatchID());
                getMatch(NEXT_GAME, teamName);
            } else if (match.getStatus().equalsIgnoreCase(NOT_STARTED)) {
                matchIds.put(NEXT_GAME, match.getMatchID());
                getMatch(LAST_GAME, teamName);
            } else {
                //if match is in progress, add all 3 matchIDs
                matchIds.put(CURRENT_GAME, match.getMatchID());
                getMatch(LAST_GAME,teamName);
                getMatch(NEXT_GAME,teamName);
            }
        } else {
            getMatch(LAST_GAME,teamName);
            getMatch(NEXT_GAME,teamName);
        }
        return matchIds;
    }

    private void getMatch(String matchType, String teamName) {
        String date = getCurrDate();
        int increment;
        if (matchType.equalsIgnoreCase(NEXT_GAME)){
            increment=1;
        } else {
            increment=-1;
        }
        //only check the last 2 weeks
        for (int i = 0;i<14;i++){
            date = increment(date,increment);
            if (date != null) {
                String response = liveScoreIntegrator.getLatestMatch(date, liveScoreConfiguration);
                Match match = getMatchByTeamName(response, teamName);
                if (match != null) {
                    matchIds.put(matchType, match.getMatchID());
                    return;
                }
            } else {
                return;
            }
        }
        log.info("No " + matchType + " match found within 2 week period for " + teamName + ".");
        matchIds.put(matchType,"None");
    }

    private String increment(String currDate,int increment) {
        try {
            Date date = new SimpleDateFormat("yyyyMMdd").parse(currDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, increment);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            log.info("New Date : " + dateFormat.format(cal.getTime()));
            return dateFormat.format(cal.getTime());
        } catch (ParseException e) {
            log.error(e);
            return null;
        }
    }

    private Match getMatchByTeamName(String jsonResponse,String teamName){
        try {
            Match match = new Match();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject dataObj = data.getJSONObject(0);
            JSONArray matches = dataObj.getJSONArray("matches");
            for (int i = 0; i < matches.length(); i++) {
                JSONObject matchObj = (JSONObject) matches.get(i);
                JSONObject team1 = matchObj.getJSONObject("team_1");
                JSONObject team2 = matchObj.getJSONObject("team_2");
                if (teamName.equalsIgnoreCase((String) team1.get("name")) ||
                        teamName.equalsIgnoreCase((String) team2.get("name"))) {
                    match.setMatchID((String) matchObj.get("match_id"));
                    match.setStatus((String) matchObj.get("status"));
                    return match;
                }
            }
            return null;
        } catch (Exception e) {
            log.info("No matches on provided date");
            return null;
        }
    }

    private String getCurrDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date());
    }
}
