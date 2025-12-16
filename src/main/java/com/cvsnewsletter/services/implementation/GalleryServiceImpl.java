package com.cvsnewsletter.services.implementation;

import com.cvsnewsletter.dtos.request.GalleryRequest;
import com.cvsnewsletter.dtos.response.GalleryResponse;
import com.cvsnewsletter.dtos.response.PagedGalleryResponse;
import com.cvsnewsletter.entities.Gallery;
import com.cvsnewsletter.entities.GalleryImage;
import com.cvsnewsletter.exception.BadRequestException;
import com.cvsnewsletter.repositories.GalleryImageRepository;
import com.cvsnewsletter.repositories.GalleryRepository;
import com.cvsnewsletter.services.GalleryService;
import com.cvsnewsletter.utility.CvsUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository repository;
    private final GalleryImageRepository imageRepository;

    @Override
    @Transactional
    public String createGallery(GalleryRequest request, List<MultipartFile> imageFiles) throws Exception {
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new IllegalArgumentException("At least one image is required.");
        }
        if (imageFiles.size() > 5) {
            throw new IllegalArgumentException("Maximum 5 images allowed per gallery.");
        }

        List<GalleryImage> galleryImages = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            if (!CvsUtility.isImageFile(file)) {
                throw new IllegalArgumentException("Invalid file type. Only image files are allowed.");
            }

            GalleryImage img = GalleryImage.builder()
                    .imageData(file.getBytes())
                    .imageName(file.getOriginalFilename())
                    .imageType(file.getContentType())
                    .build();
            galleryImages.add(img);
        }

        Gallery gallery = Gallery.builder()
                .title(request.getTitle())
                .images(galleryImages)
                .uploadDate(LocalDate.now())
                .build();

        galleryImages.forEach(img -> img.setGallery(gallery));

        repository.save(gallery);
        return "Gallery created successfully.";
    }

    @Override
    public PagedGalleryResponse getGalleries(Pageable pageable, String baseUrl) {
        Page<Gallery> page = repository.findAllByOrderByUploadDateDesc(pageable);

        List<GalleryResponse> galleryResponses = page.getContent().stream()
                .map(gallery -> GalleryResponse.builder()
                        .id(gallery.getId())
                        .title(gallery.getTitle())
                        .uploadDate(gallery.getUploadDate())
                        .imageUrls(
                                gallery.getImages().stream()
                                        .map(img -> baseUrl + "/api/v1/galleries/images/" + img.getId())
                                        .collect(Collectors.toList())
                        )
                        .build()
                )
                .collect(Collectors.toList());

        return PagedGalleryResponse.builder()
                .galleries(galleryResponses)
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public GalleryImage getImageById(Integer id) {
        return imageRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public String deleteGallery(Integer id) {
        if (!repository.existsById(id)) {
            throw new BadRequestException("Gallery not found with id: " + id);
        }
        repository.deleteById(id);
        return "Gallery deleted successfully.";
    }

    @Override
    @Transactional
    public String deleteImage(Integer id) {
        if (!imageRepository.existsById(id)) {
            throw new BadRequestException("Image not found with id: " + id);
        }
        imageRepository.deleteById(id);
        return "Image deleted successfully.";
    }

    @Override
    @Transactional
    public String updateGalleryTitle(Integer id, String newTitle) {
        Gallery gallery = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Gallery not found with id: " + id));

        gallery.setTitle(newTitle);
        repository.save(gallery);
        return "Gallery title updated successfully.";
    }

    @Override
    @Transactional
    public String updateGalleryImages(Integer id, List<MultipartFile> newImages) throws IOException {
        Gallery gallery = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Gallery not found with id: " + id));

        if (newImages == null || newImages.isEmpty()) {
            throw new BadRequestException("At least one image is required.");
        }
        if (newImages.size() > 5) {
            throw new BadRequestException("Maximum 5 images allowed per gallery.");
        }

        gallery.getImages().clear();

        for (MultipartFile file : newImages) {
            if (!CvsUtility.isImageFile(file)) {
                throw new BadRequestException("Invalid file type. Only image files are allowed.");
            }

            GalleryImage img = GalleryImage.builder()
                    .imageData(file.getBytes())
                    .imageName(file.getOriginalFilename())
                    .imageType(file.getContentType())
                    .gallery(gallery)
                    .build();

            gallery.getImages().add(img);
        }

        repository.save(gallery);
        return "Gallery images updated successfully.";
    }

}
