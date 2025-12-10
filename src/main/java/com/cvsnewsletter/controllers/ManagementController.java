package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.request.OnboardRequest;
import com.cvsnewsletter.entities.enums.Role;
import com.cvsnewsletter.services.OnboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/management")
@RequiredArgsConstructor
public class ManagementController {

    private final OnboardService service;

    @PostMapping("/onboard")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody OnboardRequest request
    ) {
        return ResponseEntity.ok(Map.of("message", service.onboard(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign-role/{ohrId}")
    public ResponseEntity<String> assignRole(@PathVariable String ohrId,
                                             @RequestParam Role role) {
        service.assignRoleToMember(ohrId, role);
        return ResponseEntity.ok("Role " + role.name() + " assigned successfully.");
    }

}
