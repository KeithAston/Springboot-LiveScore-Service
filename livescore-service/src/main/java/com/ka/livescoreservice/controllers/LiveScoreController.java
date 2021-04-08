package com.ka.livescoreservice.controllers;

import com.ka.livescoreservice.services.LiveScoreService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/LivescoreService")
@AllArgsConstructor
public class LiveScoreController {

    private LiveScoreService liveScoreService;

    @RequestMapping("/GetMatchDetails/{teamName}")
    public String getLastGameDetails(@PathVariable("teamName") String teamName){
        return liveScoreService.getDetails(teamName);
    }

}
