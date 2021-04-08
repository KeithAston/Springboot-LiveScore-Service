package com.ka.livescoreservice.models;

import lombok.Data;

@Data
public class TeamInfo {
    private String teamName;
    private Match lastMatch;
    private String nextMatchID;
    private Match currentMatch;
}
