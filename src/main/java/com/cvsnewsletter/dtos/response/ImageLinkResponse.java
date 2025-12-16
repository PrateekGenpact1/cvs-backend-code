package com.cvsnewsletter.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageLinkResponse {
    private Integer id;
    private String link;
}
