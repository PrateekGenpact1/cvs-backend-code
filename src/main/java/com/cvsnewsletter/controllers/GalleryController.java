package com.cvsnewsletter.controllers;

import com.cvsnewsletter.dtos.request.GalleryRequest;
import com.cvsnewsletter.dtos.response.PagedGalleryResponse;
import com.cvsnewsletter.entities.GalleryImage;
import com.cvsnewsletter.services.GalleryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/galleries")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService service;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> createGallery(
            @Valid @RequestPart("request") GalleryRequest request,
            @RequestPart("images") List<MultipartFile> imageFiles) {

        try {
            String saved = service.createGallery(request, imageFiles);
            return ResponseEntity.status(201).body(Map.of("message", saved));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<PagedGalleryResponse> getGalleries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        // enforce max size = 50
        if (size > 50) {
            size = 50;
        }

        Pageable pageable = PageRequest.of(page, size);
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        PagedGalleryResponse response = service.getGalleries(pageable, baseUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        GalleryImage image = service.getImageById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getImageName() + "\"")
                .contentType(MediaType.valueOf(image.getImageType()))
                .body(image.getImageData());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteGallery(@PathVariable Integer id) {
        try {
            String message = service.deleteGallery(id);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable Integer id) {
        try {
            String message = service.deleteImage(id);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{id}/title")
    public ResponseEntity<Map<String, String>> updateGalleryTitle(
            @PathVariable Integer id,
            @RequestParam String newTitle) {
        try {
            String message = service.updateGalleryTitle(id, newTitle);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping(value = "/{id}/images", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> updateGalleryImages(
            @PathVariable Integer id,
            @RequestPart("images") List<MultipartFile> newImages) {
        try {
            String message = service.updateGalleryImages(id, newImages);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

}

