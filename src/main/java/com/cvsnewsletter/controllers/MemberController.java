package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.PasswordRequest;
import com.cvsnewsletter.services.MemberService;
import com.cvsnewsletter.utility.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @GetMapping("/{ohrId}")
    public ResponseEntity<MemberDetailsDto> getMemberDetails(
            @PathVariable String ohrId
    ) {
        return ResponseEntity.ok(service.getMemberDetails(ohrId));
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> saveMemberDetails(
            @RequestBody PasswordRequest passwordRequest
            ) {
        return ResponseEntity.ok(Map.of("message", service.savePassword(passwordRequest)));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> saveMemberDetails(
            @RequestPart MemberDetailsDto memberDetailsDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !FileUtils.isImageFile(image)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid file type. Only image files are allowed.");
            }

            String message = service.saveMemberDetails(memberDetailsDto, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing image: " + ex.getMessage());

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Unexpected error occurred: " + ex.getMessage());
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateMemberDetails(
            @RequestPart MemberDetailsDto memberDetailsDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !FileUtils.isImageFile(image)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid file type. Only image files are allowed.");
            }

            String message = service.updateMemberDetails(memberDetailsDto, image);
            return ResponseEntity.ok(message);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing image: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Unexpected error occurred: " + ex.getMessage());
        }
    }


}
