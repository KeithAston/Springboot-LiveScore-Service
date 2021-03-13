package com.ka.livescoreservice.controllers;

import com.ka.livescoreservice.services.LiveScoreService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/LivescoreService")
public class LiveScoreController {

    private LiveScoreService liveScoreService;

    @RequestMapping("/getMatchDetails")
    public void getLastGameDetails(){
        liveScoreService.getDetails();
    }

}
