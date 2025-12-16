package com.cvsnewsletter.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLocationResponse {
    private String name;
    private String ohrId;
    private String seatNumber;
}
