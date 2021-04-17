package com.ka.matchinfoservice;

import com.ka.matchinfoservice.config.LiveScoreConfiguration;
import com.ka.matchinfoservice.integrators.LiveScoreIntegrator;
import com.ka.matchinfoservice.services.MatchInfoService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;


import static com.ka.matchinfoservice.TestHelper.TEAM_NAME;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class MatchInfoServiceTest {

    private static final String JSONMatchIDsByDateResponse_FILENAME = "MatchByDateAPIResponse.json";
    private static final String JSONMatchIDsLastMatchResponse_FILENAME = "LastMatchJsonResponse.json";
    private static final String DATE_PATTERN = "yyyyMMdd";

    private MatchInfoService matchInfoService;
    private TestHelper testHelper;
    private String LastMatchResponse;
    private String NextMatchResponse;

    @Mock
    private LiveScoreConfiguration liveScoreConfiguration = new LiveScoreConfiguration();

    @Mock
    private LiveScoreIntegrator liveScoreIntegrator;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        testHelper = new TestHelper();
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        //get contents of sample API response from local filesystem
        Resource nextMatchResource = resourceLoader.getResource("classpath:" + JSONMatchIDsByDateResponse_FILENAME);
        Reader nextMatchReader = new InputStreamReader(nextMatchResource.getInputStream(), UTF_8);
        LastMatchResponse = FileCopyUtils.copyToString(nextMatchReader);

        Resource lastMatchResource = resourceLoader.getResource("classpath:" + JSONMatchIDsLastMatchResponse_FILENAME);
        Reader lastMatchReader = new InputStreamReader(lastMatchResource.getInputStream(), UTF_8);
        NextMatchResponse = FileCopyUtils.copyToString(lastMatchReader);

        matchInfoService = new MatchInfoService(liveScoreIntegrator,liveScoreConfiguration,null);
    }

    @Test
    public void testGetMatchIDs() throws ParseException {
        when(liveScoreIntegrator.getLatestMatch(eq(getCurrDate()),any(LiveScoreConfiguration.class)))
                .thenReturn(LastMatchResponse);
        when(liveScoreIntegrator.getLatestMatch(eq(getYesterdaysDate()),any(LiveScoreConfiguration.class)))
                .thenReturn(NextMatchResponse);
        //confirm that given the sample API responses, the expected HashMap is returned
        assertThat(matchInfoService.getMatchIDs(TEAM_NAME)).isEqualTo(testHelper.getExpectedMatchIDsMap());
    }

    private String getYesterdaysDate() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        String currDate = dateFormat.format(new Date());
        Date date = new SimpleDateFormat(DATE_PATTERN).parse(currDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    private String getCurrDate(){
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        return dateFormat.format(new Date());
    }

}
