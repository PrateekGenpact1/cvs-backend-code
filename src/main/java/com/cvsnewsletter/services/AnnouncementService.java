package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.request.AnnouncementRequest;
import com.cvsnewsletter.entities.Announcement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AnnouncementService {
    String createAnnouncement(AnnouncementRequest request, MultipartFile imageFile) throws IOException;

    List<Announcement> findActiveAnnouncements(LocalDate today);

    void deleteAnnouncement(Integer id);
}
