package com.banker.duckrace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class LogController {
    private final RestTemplate restTemplate;

    @Autowired
    public LogController(RestTemplate restTemp) {
        this.restTemplate = restTemp;
    }
    
    @GetMapping("/log")
    public String log() {
        String logUrl = "https://arriving-emerging-boar.ngrok-free.app/log";

        try {
            // Fetch logs from the external URL
            return restTemplate.getForObject(logUrl, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving logs";
        }
    }

}
