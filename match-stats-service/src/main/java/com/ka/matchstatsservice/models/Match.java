package com.ka.matchstatsservice.models;

import lombok.Data;

@Data
public class Match {
    private String matchID;
    private MatchStats matchStats;
}
