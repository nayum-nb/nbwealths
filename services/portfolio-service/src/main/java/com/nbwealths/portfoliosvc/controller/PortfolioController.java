package com.nbwealths.portfoliosvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    @GetMapping
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("portfolio-service up");
    }
}
