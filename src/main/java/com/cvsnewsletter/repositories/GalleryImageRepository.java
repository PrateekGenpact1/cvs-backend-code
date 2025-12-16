package com.cvsnewsletter.repositories;

import com.cvsnewsletter.entities.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, Integer> {
}
