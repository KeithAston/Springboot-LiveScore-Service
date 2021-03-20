package com.ka.matchlineupsservice.models;

import lombok.Data;
import java.util.List;

@Data
public class Lineup {
    private String teamName;
    private List<String> startingPlayers;
    private String formation;
    private String manager;
}
