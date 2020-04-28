package com.example.restservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GreetingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingService.class);

    @Scheduled(fixedRateString = "${next.day.depot.refresh.interval:300000}")
    public void refreshNextDayDepotList() {

        final String uri = "http://www.google.co.uk/";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> out = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        LOGGER.info("Got response status {}", out.getStatusCode());
    }

}
