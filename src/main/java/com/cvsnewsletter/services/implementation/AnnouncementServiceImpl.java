package com.cvsnewsletter.services.implementation;

import com.cvsnewsletter.dtos.request.AnnouncementRequest;
import com.cvsnewsletter.entities.Announcement;
import com.cvsnewsletter.exception.BadRequestException;
import com.cvsnewsletter.repositories.AnnouncementRepository;
import com.cvsnewsletter.services.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository repository;

    @Override
    @Transactional
    public String createAnnouncement(AnnouncementRequest request, MultipartFile imageFile) throws IOException {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        Announcement announcement = Announcement.builder()
                .imageData(imageFile.getBytes())
                .imageName(imageFile.getOriginalFilename())
                .imageType(imageFile.getContentType())
                .message(request.getMessage())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        repository.save(announcement);

        return "Announcement details saved successfully";
    }

    @Override
    public List<Announcement> findActiveAnnouncements(LocalDate today) {
        return repository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Integer id) {
        if (!repository.existsById(id)) {
            throw new BadRequestException("Announcement not found");
        }
        repository.deleteById(id);
    }

}
