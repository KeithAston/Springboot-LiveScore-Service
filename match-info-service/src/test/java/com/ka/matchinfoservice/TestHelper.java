package com.ka.matchinfoservice;

import java.util.HashMap;
import java.util.Map;

public class TestHelper {

    public static final String TEAM_NAME = "Liverpool";

    public Map<String, String> getExpectedMatchIDsMap(){
        Map<String,String> matchIds = new HashMap<>();
        matchIds.put("next","267673");
        matchIds.put("last", "267586");
        return matchIds;
    }
}
