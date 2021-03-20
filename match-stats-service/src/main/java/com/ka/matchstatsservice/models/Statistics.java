package com.ka.matchstatsservice.models;

import lombok.Data;

@Data
public class Statistics {

    private String teamName;
    private String goalsScored;
    private int possession;
    private int crosses;
    private int shotsOnTarget;
    private int fouls;
    private int yellowCards;
    private int redCards;

}
