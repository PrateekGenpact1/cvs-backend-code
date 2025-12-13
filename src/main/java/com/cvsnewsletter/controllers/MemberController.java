package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.LimitedMemberDetailsDto;
import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.PasswordRequest;
import com.cvsnewsletter.entities.Member;
import com.cvsnewsletter.exception.BadRequestException;
import com.cvsnewsletter.repositories.MemberRepository;
import com.cvsnewsletter.services.MemberService;
import com.cvsnewsletter.utility.CvsUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final MemberRepository repository;

    @GetMapping("/{ohrId}")
    public ResponseEntity<LimitedMemberDetailsDto> getMemberDetails(@PathVariable String ohrId) {
        return ResponseEntity.ok(service.getMemberDetails(ohrId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    @GetMapping("/{ohrId}/details")
    public ResponseEntity<MemberDetailsDto> getMemberFullDetails(@PathVariable String ohrId) {
        return ResponseEntity.ok(service.getFullMemberDetails(ohrId));
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> saveMemberDetails(
            @RequestBody PasswordRequest passwordRequest
            ) {
        return ResponseEntity.ok(Map.of("message", service.savePassword(passwordRequest)));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    public ResponseEntity<Map<String, String>> saveMemberDetails(
            @RequestPart MemberDetailsDto memberDetailsDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            if (image != null && !CvsUtility.isImageFile(image)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message","Invalid file type. Only image files are allowed."));
            }

            String message = service.saveMemberDetails(memberDetailsDto, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", message));
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Error processing image: " + ex.getMessage()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Unexpected error occurred: " + ex.getMessage()));
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateMemberDetails(
            @RequestPart MemberDetailsDto memberDetailsDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !CvsUtility.isImageFile(image)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid file type. Only image files are allowed."));
            }

            String message = service.updateMemberDetails(memberDetailsDto, image);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Error processing image: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Unexpected error occurred: " + ex.getMessage()));
        }
    }

    @GetMapping("/{ohrId}/image")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    public ResponseEntity<byte[]> getMemberImage(@PathVariable String ohrId) {
        Member member = repository.findByOhrId(ohrId)
                .orElseThrow(() -> new BadRequestException("Member not found with OHR: " + ohrId));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + member.getImageName() + "\"")
                .contentType(MediaType.valueOf(member.getImageType()))
                .body(member.getImageData());
    }

}
