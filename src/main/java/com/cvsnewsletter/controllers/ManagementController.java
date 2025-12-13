package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.request.AssignRoleRequest;
import com.cvsnewsletter.dtos.request.OnboardRequest;
import com.cvsnewsletter.services.OnboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/assign-role")
    public ResponseEntity<Map<String, String>> assignRole(@RequestBody AssignRoleRequest request) {
        String ohrId = request.getOhrId();
        String role = request.getRole();
        service.assignRoleToMember(ohrId, role);
        return ResponseEntity.ok(Map.of(
                "message",
                String.format("Role %s assigned successfully to member with OHR ID %s", role, ohrId)
        ));
    }

}
