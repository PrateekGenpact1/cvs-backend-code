package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.response.ApprovedKudosResponse;
import com.cvsnewsletter.dtos.response.KudosResponse;
import com.cvsnewsletter.services.KudosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/kudos")
@RequiredArgsConstructor
public class KudosController {

    private final KudosService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unapproved")
    public ResponseEntity<List<KudosResponse>> getUnapprovedKudos() {
        return ResponseEntity.ok(service.getUnapprovedKudos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String, String>> approveKudos(@PathVariable Integer id) {
        String message = service.approveKudos(id);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    @GetMapping("/approved")
    public ResponseEntity<List<ApprovedKudosResponse>> getApprovedKudosWithImages() {
        return ResponseEntity.ok(service.getApprovedKudosWithImages());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteKudos(@PathVariable Integer id) {
        String message = service.deleteKudos(id);
        return ResponseEntity.ok(Map.of("message", message));
    }

}
