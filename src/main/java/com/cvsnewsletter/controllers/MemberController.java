package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @GetMapping("/{ohrId}")
    public ResponseEntity<MemberDetailsDto> getMemberDetails(
            @RequestParam String ohrId
    ) {
        return ResponseEntity.ok(service.getMemberDetails(ohrId));
    }

    @PostMapping("/register")
    public ResponseEntity<String> saveMemberDetails(
            @RequestPart MemberDetailsDto memberDetailsDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            String message = service.saveMemberDetails(memberDetailsDto, image);

            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing image: " + ex.getMessage());

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + ex.getMessage());
        }
    }

}
