package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.dtos.response.MemberLocationResponse;
import com.cvsnewsletter.services.MemberService;
import com.cvsnewsletter.utility.CvsUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
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

    @GetMapping("/seating-arrangement")
    public ResponseEntity<List<MemberLocationResponse>> getMembersByLocation(
            @RequestParam String location) {
        List<MemberLocationResponse> members = service.getMembersByLocation(location);
        return ResponseEntity.ok(members);
    }


}
