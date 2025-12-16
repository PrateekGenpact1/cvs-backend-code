package com.cvsnewsletter.dtos.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryResponse {
    private Integer id;
    private String title;
    private LocalDate uploadDate;
    private List<String> imageUrls;
}
