package com.ka.matchinfoservice.controllers;

import com.ka.matchinfoservice.services.MatchInfoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/matchInfo")
@AllArgsConstructor
public class MatchInfoController {

    private MatchInfoService matchInfoService;

    @GetMapping(value = "/{teamName}")
    public Map<String, String> getMatchIDsBeingPlayedToday(@PathVariable(value = "teamName") String teamName){
        return matchInfoService.getMatchIDs(teamName);
    }


}
