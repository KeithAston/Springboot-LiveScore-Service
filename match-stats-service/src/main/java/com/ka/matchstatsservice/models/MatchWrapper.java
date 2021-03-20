package com.ka.matchstatsservice.models;

import lombok.Data;

import java.util.List;

@Data
public class MatchWrapper {
    private List<String> matchIDs;
}
