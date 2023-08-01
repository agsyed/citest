package com.syed.citest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalAPICaller {
    private final RestTemplate restTemplate;

    @Autowired
    public ExternalAPICaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callApi() {
        return restTemplate.getForObject("/hello/test", String.class);
    }

    public String callApiWithDelay() {
        String result = restTemplate.getForObject("/hello/test", String.class);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignore) {
        }
        return result;
    }
}