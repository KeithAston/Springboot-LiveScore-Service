package com.ka.matchstatsservice.models;

import lombok.Data;

@Data
public class MatchStats {
    private Statistics team1;
    private Statistics team2;
}
