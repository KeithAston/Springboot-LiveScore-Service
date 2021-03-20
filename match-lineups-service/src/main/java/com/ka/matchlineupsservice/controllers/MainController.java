package com.ka.matchlineupsservice.controllers;

import com.ka.matchlineupsservice.models.Match;
import com.ka.matchlineupsservice.models.MatchWrapper;
import com.ka.matchlineupsservice.services.MatchLineupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/")
@AllArgsConstructor
public class MainController {

    private MatchLineupService matchLineupService;

    @PostMapping(value = "/matchlineups", consumes = "application/json")
    @ResponseBody
    public List<Match> getMatchLienups(@RequestBody MatchWrapper matchIDs){
        return matchLineupService.getMatchLineups(matchIDs);
    }

}
