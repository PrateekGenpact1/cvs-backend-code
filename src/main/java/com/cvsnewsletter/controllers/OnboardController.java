package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.request.OnboardRequest;
import com.cvsnewsletter.services.OnboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/management")
@RequiredArgsConstructor
public class OnboardController {

    private final OnboardService service;

    @PostMapping("/onboard")
    public ResponseEntity<String> register(
            @RequestBody OnboardRequest request
    ) {
        return ResponseEntity.ok(service.onboard(request));
    }
}
