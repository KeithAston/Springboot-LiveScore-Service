package com.ka.matchinfoservice.clients;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(name = "${com.ka.livescore.api-name}", url = "${com.ka.livescore.api-url}")
@Headers("Content-Type: application/json")
public interface LiveScoreApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/matches-by-date")
    String getMatchesByDate(@RequestHeader("x-rapidapi-key") String token,
                            @RequestHeader("x-rapidapi-host") String host,
                            @RequestParam(value = "date") String date,
                            @RequestParam(value = "league_code") String league_code,
                            @RequestParam(value = "timezone_utc") String timezone_utc,
                            @RequestParam(value = "country_code") String country_code);

}
