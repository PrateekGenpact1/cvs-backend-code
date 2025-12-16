package com.cvsnewsletter.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSummaryResponse {
    private String firstName;
    private String lastName;
    private String applicationArea;
    private String tower;
    private String reportingManager;
    private String birthday;
    private String anniversary;
    private String baseLocation;
    private String seatNumber;
}

