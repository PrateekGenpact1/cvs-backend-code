package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.request.GalleryRequest;
import com.cvsnewsletter.dtos.response.PagedGalleryResponse;
import com.cvsnewsletter.entities.GalleryImage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GalleryService {

    String createGallery(GalleryRequest request, List<MultipartFile> imageFiles) throws Exception;

    PagedGalleryResponse getGalleries(Pageable pageable, String baseUrl);

    GalleryImage getImageById(Integer id);

    String deleteGallery(Integer id);

    String deleteImage(Integer id);

    String updateGalleryTitle(Integer id, String newTitle);

    String updateGalleryImages(Integer id, List<MultipartFile> newImages) throws IOException;
}
