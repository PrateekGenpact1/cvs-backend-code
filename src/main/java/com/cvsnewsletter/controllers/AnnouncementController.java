package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.request.AnnouncementRequest;
import com.cvsnewsletter.entities.Announcement;
import com.cvsnewsletter.services.AnnouncementService;
import com.cvsnewsletter.utility.CvsUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService service;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> createAnnouncement(
            @Valid @RequestPart("request") AnnouncementRequest request,
            @RequestPart("image") MultipartFile imageFile) {

        try {
            if (imageFile == null || imageFile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Image file is required."));
            }

            if (!CvsUtility.isImageFile(imageFile)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message","Invalid file type. Only image files are allowed."));
            }

            String saved = service.createAnnouncement(request, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", saved));
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Error processing image: " + ex.getMessage()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Unexpected error occurred: " + ex.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Announcement>> getActiveAnnouncements() {
        LocalDate today = LocalDate.now();
        List<Announcement> activeAnnouncements = service.findActiveAnnouncements(today);
        return ResponseEntity.ok(activeAnnouncements);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAnnouncement(@PathVariable Integer id) {
        try {
            service.deleteAnnouncement(id);
            return ResponseEntity.ok(Map.of("message", "Announcement deleted successfully."));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Announcement not found with id: " + id));
        }
    }

}

