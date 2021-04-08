package com.ka.livescoreservice.services;

import com.ka.livescoreservice.integrators.MatchServicesIntegrator;
import com.ka.livescoreservice.models.*;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@CommonsLog
public class LiveScoreService {

    private MatchServicesIntegrator matchServicesIntegrator;
    private static final String CURRENT_KEY = "current";
    private static final String LAST_KEY = "last";
    private static final String NEXT_KEY = "next";
    private static final String TEAM_1 = "team1";
    private static final String TEAM_2 = "team2";
    private static final String TEAM_1_LINEUP = "team1Lineup";
    private static final String TEAM_2_LINEUP = "team2Lineup";
    private static final String MATCH_ID = "matchID";
    private static final String MATCH_STATS = "matchStats";
    private static final String TEAM_NAME = "teamName";
    private static final String GOALS_SCORED = "goalsScored";
    private static final String POSSESSION = "possession";
    private static final String CROSSES = "crosses";
    private static final String SHOTS_ON_TARGET = "shotsOnTarget";
    private static final String FOULS = "fouls";
    private static final String YELLOW_CARDS = "yellowCards";
    private static final String RED_CARDS = "redCards";
    private static final String MANAGER = "manager";
    private static final String FORMATION = "formation";
    private static final String STARTING_PLAYERS = "startingPlayers";


    public String getDetails(String teamName){
        MatchWrapper matchWrapper = new MatchWrapper();
        TeamInfo teamInfo = new TeamInfo();
        teamInfo.setTeamName(teamName);

        //get matchIDs from match-info-service
        String matchIDResponse = matchServicesIntegrator.getMatchIDs(teamName);

        JSONObject jsonObject = new JSONObject(matchIDResponse);
        List<String> ids = new ArrayList<>();

        //if the chosen team has a game currently ongoing, add the stats.
        if (!jsonObject.isNull(CURRENT_KEY)){
            ids.add(jsonObject.getString(CURRENT_KEY));
        }
        ids.add(jsonObject.getString(LAST_KEY));
        teamInfo.setNextMatchID(jsonObject.getString(NEXT_KEY));
        matchWrapper.setMatchIDs(ids);

        //call match-stats-service and match-lineups-service for given matchIDs
        String matchStatsResponse = matchServicesIntegrator.getMatchStats(matchWrapper);
        String matchLineupsResponse = matchServicesIntegrator.getMatchLineups(matchWrapper);

        teamInfo.setLastMatch(getMatch(LAST_KEY,jsonObject,matchLineupsResponse,matchStatsResponse));
        if (!jsonObject.isNull(CURRENT_KEY)){
            teamInfo.setCurrentMatch(getMatch(CURRENT_KEY,jsonObject,matchLineupsResponse,matchStatsResponse));
        }
        return teamInfo.toString();
    }

    private Match getMatch(String matchType, JSONObject jsonObject, String lineupResponse, String statsResponse){
        Match match = new Match();
        match.setMatchID(jsonObject.getString(matchType));
        match.setTeam1Lineup(getTeamLineup(TEAM_1_LINEUP, match.getMatchID(), lineupResponse));
        match.setTeam2Lineup(getTeamLineup(TEAM_2_LINEUP, match.getMatchID(), lineupResponse));
        match.setTeam1Stats(getTeamStats(TEAM_1, match.getMatchID(), statsResponse));
        match.setTeam2Stats(getTeamStats(TEAM_2, match.getMatchID(), statsResponse));
        return match;
    }

    private Statistics getTeamStats(String team, String matchID, String jsonResponse) {
        Statistics statistics = new Statistics();
        JSONArray matches = new JSONArray(jsonResponse);
        for (int i =0; i< matches.length();i++){
            JSONObject match = (JSONObject) matches.get(i);
            if (match.getString(MATCH_ID).equalsIgnoreCase(matchID)){
                JSONObject matchStats = (JSONObject) match.get(MATCH_STATS);
                //get stats for given team (team1 or team2)
                JSONObject teamStats = matchStats.getJSONObject(team);
                //populate Statistics object with json values
                statistics.setTeamName(teamStats.getString(TEAM_NAME));
                statistics.setGoalsScored(teamStats.getString(GOALS_SCORED));
                statistics.setPossession(teamStats.getInt(POSSESSION));
                statistics.setCrosses(teamStats.getInt(CROSSES));
                statistics.setShotsOnTarget(teamStats.getInt(SHOTS_ON_TARGET));
                statistics.setFouls(teamStats.getInt(FOULS));
                statistics.setYellowCards(teamStats.getInt(YELLOW_CARDS));
                statistics.setRedCards(teamStats.getInt(RED_CARDS));
                return statistics;
            }
        }
        return null;
    }

    private Lineup getTeamLineup(String team, String matchID, String jsonResponse){
        Lineup lineup = new Lineup();
        JSONArray matches = new JSONArray(jsonResponse);
        for (int i =0; i< matches.length();i++){
            JSONObject match = (JSONObject) matches.get(i);
            if (match.getString(MATCH_ID).equalsIgnoreCase(matchID)){

                JSONObject teamLineup = match.getJSONObject(team);

                lineup.setManager(teamLineup.getString(MANAGER));
                lineup.setTeamName(teamLineup.getString(TEAM_NAME));
                lineup.setFormation(teamLineup.getString(FORMATION));
                JSONArray jsonArray = teamLineup.getJSONArray(STARTING_PLAYERS);
                List<String> players = new ArrayList<>();
                for(int j=0;j< jsonArray.length();j++){
                    players.add(jsonArray.getString(j));
                }
                lineup.setStartingPlayers(players);
                return lineup;
            }
        }
        return null;
    }

}
