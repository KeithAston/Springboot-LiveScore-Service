package com.ka.matchlineupsservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "${com.ka.livescore.api-name}", url = "${com.ka.livescore.api-url}")
public interface LivescoreApiClient {

    @GetMapping(value = "/match-lineups")
    String getMatchLineups(@RequestHeader("x-rapidapi-key") String token,
                           @RequestHeader("x-rapidapi-host") String host,
                           @RequestParam("match_id") String matchID);
}
