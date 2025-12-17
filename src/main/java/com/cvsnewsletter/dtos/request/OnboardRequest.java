package com.cvsnewsletter.dtos.request;

import com.cvsnewsletter.annotation.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnboardRequest {
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "OHR ID is mandatory")
    private String ohrId;

    @NotBlank(message = "Mobile number is mandatory")
    private String mobileNumber;

    @ValidRole
    private String role;

    @NotBlank(message = "Emergency contact name is mandatory")
    private String emergencyContactName;

    @NotBlank(message = "Emergency phone number is mandatory")
    private String emergencyPhoneNumber;
}
