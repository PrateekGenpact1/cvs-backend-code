package com.cvsnewsletter.dtos.request;

import com.cvsnewsletter.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnboardRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String ohrId;
    private String mobileNumber;
    private Role role;
}
