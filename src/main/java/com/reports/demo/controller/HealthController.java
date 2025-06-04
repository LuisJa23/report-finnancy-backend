package com.reports.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @GetMapping("/")
    public Map<String, String> root() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "Finnancy API");
        response.put("message", "Application is running successfully");
        response.put("port", System.getProperty("server.port", "5000"));
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", new java.util.Date().toString());
        response.put("port", System.getProperty("server.port", "5000"));
        
        // Test MongoDB connectivity
        try {
            if (mongoTemplate != null) {
                mongoTemplate.getCollectionNames();
                response.put("mongodb", "CONNECTED");
            } else {
                response.put("mongodb", "NOT_CONFIGURED");
            }
        } catch (Exception e) {
            response.put("mongodb", "ERROR: " + e.getMessage());
        }
        
        return response;
    }
}
