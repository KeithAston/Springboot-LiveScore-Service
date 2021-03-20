package com.ka.matchstatsservice.clients;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "${com.ka.livescore.api-name}", url = "${com.ka.livescore.api-url}")
@Headers("Content-Type: application/json")
public interface LivescoreApiClient {

    @GetMapping(value = "/match-statistics")
    String getMatchStats(@RequestHeader("x-rapidapi-key") String token,
                             @RequestHeader("x-rapidapi-host") String host,
                             @RequestParam(value = "match_id") String matchID);



}
