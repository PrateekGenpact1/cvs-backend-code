package com.cvsnewsletter.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LimitedMemberDetailsDto {
    private String firstName;
    private String lastName;
    private String ohrId;
    private String emailId;
    private String mobileNumber;
    private String role;
    private Boolean isInitialPasswordSet;
    private String emergencyContactName;
    private String emergencyPhoneNumber;
}
