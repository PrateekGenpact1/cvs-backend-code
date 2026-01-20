package com.cvsnewsletter.controllers;

import com.cvsnewsletter.services.DropdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dropdowns")
@RequiredArgsConstructor
public class DropdownController {

    private final DropdownService dropdownService;

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getDropdowns() {
        return ResponseEntity.ok(dropdownService.getDropdownValues());
    }
}
