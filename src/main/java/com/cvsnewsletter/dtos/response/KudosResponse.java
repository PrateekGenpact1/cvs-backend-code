package com.cvsnewsletter.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KudosResponse {
    private Integer id;
    private String ohrId;
    private String name;
    private String designation;
    private String recipientOhrId;
    private String recipientName;
    private String message;
    private String date;
    private Boolean isApproved;
}
