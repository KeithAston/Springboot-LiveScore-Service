package com.ka.matchlineupsservice.services;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ka.matchlineupsservice.config.LiveScoreConfiguration;
import com.ka.matchlineupsservice.integrators.LiveScoreIntegrator;
import com.ka.matchlineupsservice.models.Lineup;
import com.ka.matchlineupsservice.models.Match;
import com.ka.matchlineupsservice.models.MatchWrapper;
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
public class MatchLineupService {
    private final LiveScoreIntegrator liveScoreIntegrator;
    private final LiveScoreConfiguration liveScoreConfiguration;

    private static final String TEAM_1 = "team_1";
    private static final String TEAM_2 = "team_2";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String FORMATION = "formation";
    private static final String NAME = "name";
    private static final String DATA = "data";
    private static final String COACH = "coach";
    private static final String PLAYERS = "players";

    private List<Match> matchList;

    public List<Match> getMatchLineups(MatchWrapper matchIDs){
        String jsonResponse;
        for (String matchID : matchIDs.getMatchIDs()){
            jsonResponse = liveScoreIntegrator.getLineups(matchID,liveScoreConfiguration);
            Match match = new Match();
            match.setMatchID(matchID);
            match.setTeam1Lineup(getTeamLineup(TEAM_1,jsonResponse));
            match.setTeam2Lineup(getTeamLineup(TEAM_2,jsonResponse));
            matchList.add(match);
        }
        return matchList;
    }

    private Lineup getTeamLineup(String team, String jsonResponse){
        try {
            List<String> startingPlayers = new ArrayList<>();
            Lineup lineup = new Lineup();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject data = jsonObject.getJSONObject(DATA);
            JSONObject teamLineups = data.getJSONObject(team);
            JSONArray players = teamLineups.getJSONArray(PLAYERS);
            JSONArray managerDetails = teamLineups.getJSONArray(COACH);

            //manager is always the first index in the coach json array
            JSONObject manager = managerDetails.getJSONObject(0);
            lineup.setManager(manager.get(FIRST_NAME) + " " + manager.get(LAST_NAME));

            //loop through players and add to local list
            for (int i=0; i < players.length();i++){
                JSONObject player = (JSONObject) players.get(i);
                //check to remove 'null' from player names with only on name, e.g. Ronaldinho
                if ((!player.isNull(FIRST_NAME) && !player.isNull(LAST_NAME))){
                    startingPlayers.add((player.get(FIRST_NAME)) + " " +
                            player.get(LAST_NAME));
                } else if (!player.isNull(FIRST_NAME)){
                    startingPlayers.add((String) player.get(FIRST_NAME));
                } else {
                    startingPlayers.add((String) player.get(LAST_NAME));
                }
            }
            lineup.setStartingPlayers(startingPlayers);
            lineup.setFormation(teamLineups.getString(FORMATION));
            lineup.setTeamName(teamLineups.getString(NAME));
            return lineup;
        } catch (Exception e) {
            log.error("Error parsing JSON : " + e);
            return null; //TODO: Throw custom exception here
        }
    }

}
