package com.ka.matchstatsservice.services;

import com.ka.matchstatsservice.config.LiveScoreConfiguration;
import com.ka.matchstatsservice.integrators.LiveScoreIntegrator;
import com.ka.matchstatsservice.models.Match;
import com.ka.matchstatsservice.models.MatchStats;
import com.ka.matchstatsservice.models.MatchWrapper;
import com.ka.matchstatsservice.models.Statistics;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@CommonsLog
public class MatchStatsService {

    private static final String TEAM_1 = "team_1";
    private static final String TEAM_2 = "team_2";
    private static final String STATUS = "status";
    private static final String DATA = "data";
    private static final String CROSSES = "crosses";
    private static final String FOULS = "fouls";
    private static final String SCORE = "score";
    //intentional misspelling of possession below as json response key listed as 'possesion'
    private static final String POSSESSION = "possesion";
    private static final String SHOTS_ON_TARGET = "shots_on_target";
    private static final String NAME = "name";
    private static final String YELLOW_CARDS = "yellow_cards";
    private static final String RED_CARDS = "red_cards";

    private final LiveScoreIntegrator liveScoreIntegrator;
    private final LiveScoreConfiguration liveScoreConfiguration;

    private List<Match> matchList;

    public List<Match> getMatchStatistics(MatchWrapper matchIDs){
        String jsonResponse;
        matchList = new ArrayList<>();
        for (String matchID : matchIDs.getMatchIDs()){
            jsonResponse = liveScoreIntegrator.getMatchStats(matchID, liveScoreConfiguration);
            MatchStats matchStats = new MatchStats();
            Match match = new Match();
            match.setMatchID(matchID);
            matchStats.setTeam1(getTeamStats(TEAM_1, jsonResponse));
            matchStats.setTeam2(getTeamStats(TEAM_2, jsonResponse));
            match.setMatchStats(matchStats);
            matchList.add(match);
        }
        return matchList;
    }

    private Statistics getTeamStats(String team, String jsonResponse){
        try {
            Statistics stats = new Statistics();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.getInt(STATUS) == 200 && !jsonObject.isNull(DATA)){
                JSONObject data = jsonObject.getJSONObject(DATA);
                JSONObject teamStats = data.getJSONObject(team);
                stats.setCrosses(teamStats.getInt(CROSSES));
                stats.setFouls(teamStats.getInt(FOULS));
                stats.setGoalsScored(teamStats.getString(SCORE));
                stats.setPossession(teamStats.getInt(POSSESSION));
                stats.setShotsOnTarget(teamStats.getInt(SHOTS_ON_TARGET));
                stats.setTeamName(teamStats.getString(NAME));
                stats.setYellowCards(teamStats.getInt(YELLOW_CARDS));
                stats.setRedCards(teamStats.getInt(RED_CARDS));
            }
            return stats;
        } catch (Exception e){
            log.error("Error parsing JSON : " + e);
            return null; //TODO: throw custom exception
        }
    }




}
