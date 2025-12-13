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
public class MemberDetailsDto {
    private String firstName;
    private String lastName;
    private String ohrId;
    private String mobileNumber;
    private String emailId;
    private String applicationArea;
    private String tower;
    private String reportingManager;
    private String genpactOnsiteSpoc;
    private String baseLocation;
    private String primarySkill;
    private String currentWorkingSkills;
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
    private String emergencyContactName;
    private String emergencyPhoneNumber;
    private Boolean isRegistrationDone;
    private String password;
    private String seatNumber;
    private Boolean isInitialPasswordSet;
}
