package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.request.OnboardRequest;
import com.cvsnewsletter.dtos.response.MemberResponse;
import com.cvsnewsletter.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @GetMapping("/{ohrId}")
    public ResponseEntity<MemberResponse> getMemberDetails(
            @RequestParam String ohrId
    ) {
        return ResponseEntity.ok(service.getMemberDetails(ohrId));
    }

}
