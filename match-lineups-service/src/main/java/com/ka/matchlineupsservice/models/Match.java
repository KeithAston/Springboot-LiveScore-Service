package com.ka.matchlineupsservice.models;

import lombok.Data;

@Data
public class Match {
    private String matchID;
    private Lineup team1Lineup;
    private Lineup team2Lineup;
}
