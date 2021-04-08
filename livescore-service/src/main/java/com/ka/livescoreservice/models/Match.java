package com.ka.livescoreservice.models;

import lombok.Data;

@Data
public class Match {
    private String matchID;
    private Lineup team1Lineup;
    private Lineup team2Lineup;
    private Statistics team1Stats;
    private Statistics team2Stats;
}
