package com.cvsnewsletter.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberHierarchy {
    private String ohrId;
    private String name;
    private String reportingManagerOhrId;
}
