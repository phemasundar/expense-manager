package com.expensetracker.expensetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class HealthController {

    @Operation(summary = "Check the health of the application")
    @GetMapping("/api/health")
    public Map<String, String> healthCheck() {
        return Collections.singletonMap("status", "UP");
    }
}
