package com.ka.matchstatsservice.controllers;

import com.ka.matchstatsservice.models.Match;
import com.ka.matchstatsservice.models.MatchStats;
import com.ka.matchstatsservice.models.MatchWrapper;
import com.ka.matchstatsservice.services.MatchStatsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/")
@AllArgsConstructor
public class MatchStatsController {

    private MatchStatsService matchStatsService;

    @PostMapping(value = "/matchstats", consumes = "application/json")
    @ResponseBody
    public List<Match> getMatchStats(@RequestBody MatchWrapper matchIDs){
        return matchStatsService.getMatchStatistics(matchIDs);
    }

}
