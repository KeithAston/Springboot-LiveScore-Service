package com.ka.matchstatsservice.services;

import com.ka.matchstatsservice.config.LiveScoreConfiguration;
import com.ka.matchstatsservice.integrators.LiveScoreIntegrator;
import com.ka.matchstatsservice.models.Match;
import com.ka.matchstatsservice.models.MatchWrapper;
import com.ka.matchstatsservice.models.Statistics;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@CommonsLog
public class MatchStatsService {

    private final LiveScoreIntegrator liveScoreIntegrator;
    private final LiveScoreConfiguration liveScoreConfiguration;
    private static final String TEAM_1 = "team_1";
    private static final String TEAM_2 = "team_2";
    private List<Match> matchList;

    public List<Match> getMatchStatistics(MatchWrapper matchIDs){
        String jsonResponse;

        for (String matchID : matchIDs.getMatchIDs()){
            jsonResponse = liveScoreIntegrator.getMatchStats(matchID, liveScoreConfiguration);
            Match match = new Match();
            match.setMatchID(matchID);
            match.setTeam1(getTeamStats(TEAM_1, jsonResponse));
            match.setTeam2(getTeamStats(TEAM_2, jsonResponse));
            matchList.add(match);
        }
        return matchList;
    }

    private Statistics getTeamStats(String team, String jsonResponse){
        try {
            Statistics stats = new Statistics();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject teamStats = data.getJSONObject(team);
            stats.setCrosses(teamStats.getInt("crosses"));
            stats.setFouls(teamStats.getInt("fouls"));
            stats.setGoalsScored(teamStats.getString("score"));
            stats.setPossession(teamStats.getInt("possesion"));
            stats.setShotsOnTarget(teamStats.getInt("shots_on_target"));
            stats.setTeamName(teamStats.getString("name"));
            stats.setYellowCards(teamStats.getInt("yellow_cards"));
            stats.setRedCards(teamStats.getInt("red_cards"));
            return stats;
        } catch (Exception e){
            log.error("Error parsing JSON : " + e);
            return null; //TODO: throw custom exception
        }
    }




}
