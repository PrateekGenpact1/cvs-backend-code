package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.AssignRoleRequest;
import com.cvsnewsletter.dtos.request.KudosRequest;
import com.cvsnewsletter.dtos.request.OnboardRequest;
import com.cvsnewsletter.services.KudosService;
import com.cvsnewsletter.services.OnboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/management")
@RequiredArgsConstructor
public class ManagementController {

    private final OnboardService service;
    private final KudosService kudosService;

    @PostMapping("/onboard")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody OnboardRequest request
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

    @GetMapping("/teams")
    public ResponseEntity<List<MemberDetailsDto>> getAllMemberDetails() {
        return ResponseEntity.ok(service.getAllMemberDetails());
    }

    @PostMapping("/kudos")
    public ResponseEntity<Map<String, String>> giveKudos(@RequestBody KudosRequest request) {
        String message = kudosService.giveKudos(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", message));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{ohrId}/initial-password")
    public ResponseEntity<Map<String, String>> updateInitialPasswordFlag(@PathVariable String ohrId) {
        String message = service.updateInitialPasswordFlag(ohrId);
        return ResponseEntity.ok(Map.of("message", message));
    }

}
