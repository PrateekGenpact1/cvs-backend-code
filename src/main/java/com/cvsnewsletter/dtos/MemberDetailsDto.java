package com.cvsnewsletter.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDetailsDto {
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "OHR ID is mandatory")
    private String ohrId;

    @NotBlank(message = "Mobile number is mandatory")
    private String mobileNumber;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is mandatory")
    @JsonProperty("emailId")
    private String emailId;

    private String applicationArea;
    private String tower;

    @NotBlank(message = "Reporting Manager OHR ID is mandatory")
    private String reportingManagerOhrId;

    private String reportingManager;
    private String genpactOnsiteSpoc;
    private String baseLocation;
    private String primarySkill;
    private List<String> secondarySkill;
    private String designationBand;
    private String cvsLead;
    private String clientManager;
    private String zid;
    private String overallExperience;
    private String cvsExperience;
    private String genpactExperience;
    private String technicalExpertise;
    private String ssn;
    private String cvsEmpId;
    private String cvsMailId;
    private String highestDegree;
    private String birthday;
    private String anniversary;
    private String currentAddress;

    @NotBlank(message = "Emergency contact name is mandatory")
    private String emergencyContactName;

    @NotBlank(message = "Emergency phone number is mandatory")
    private String emergencyPhoneNumber;

    private Boolean isRegistrationDone;
    private String password;
    private String seatNumber;
    private Boolean isInitialPasswordSet;

}
