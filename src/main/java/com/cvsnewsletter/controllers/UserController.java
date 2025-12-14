package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final MemberService service;

    @PatchMapping
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        return ResponseEntity.ok(service.changePassword(request, connectedUser));
    }

    @GetMapping("/details")
    public ResponseEntity<List<MemberDetailsDto>> getAllMemberDetails() {
        return ResponseEntity.ok(service.getAllMemberDetails());
    }

}
