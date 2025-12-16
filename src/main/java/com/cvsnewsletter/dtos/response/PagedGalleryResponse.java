package com.cvsnewsletter.dtos.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedGalleryResponse {
    private List<GalleryResponse> galleries;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}

