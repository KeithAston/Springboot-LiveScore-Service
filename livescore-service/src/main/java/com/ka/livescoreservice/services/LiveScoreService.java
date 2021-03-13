package com.ka.livescoreservice.services;

import com.ka.livescoreservice.integrators.MatchInfoIntegrator;
import org.springframework.stereotype.Service;

@Service
public class LiveScoreService {

    private MatchInfoIntegrator matchInfoIntegrator;

    public void getDetails(){
        matchInfoIntegrator.getMatchIDs();


    }


}
