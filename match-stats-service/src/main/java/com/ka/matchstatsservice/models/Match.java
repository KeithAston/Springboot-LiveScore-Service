package com.ka.matchstatsservice.models;

import lombok.Data;

@Data
public class Match {
    private String matchID;
    private Statistics team1;
    private Statistics team2;
}
