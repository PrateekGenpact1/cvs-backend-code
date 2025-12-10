package com.cvsnewsletter.implementation;

import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.dtos.request.PasswordRequest;
import com.cvsnewsletter.entities.Member;
import com.cvsnewsletter.entities.enums.Role;
import com.cvsnewsletter.exception.BadRequestException;
import com.cvsnewsletter.repositories.MemberRepository;
import com.cvsnewsletter.services.MemberService;
import com.cvsnewsletter.utility.CvsUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDetailsDto getMemberDetails(String ohrId) {

        if (!CvsUtility.isValidOhrId(ohrId)) {
            throw new BadRequestException("OHR ID must be a 9-digit numeric value.");
        }

        Member memberDetails = repository.findByOhrId(ohrId)
                .orElseThrow(() -> new BadRequestException("Member not found with OHR: " + ohrId));

        return memberDetailsBuilder(memberDetails);
    }

    @Override
    public String changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new BadRequestException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);

        return "Password changed successfully!!!";
    }

    @Override
    public String saveMemberDetails(MemberDetailsDto memberDetails, MultipartFile image) throws IOException {
        if(repository.existsByOhrIdAndIsRegistrationDoneTrue(memberDetails.getOhrId())) {
            throw new BadRequestException("Registration is already completed, please proceed with the edit");
        }

        repository.save(saveMemberEntity(memberDetails, image));

        return "Member details saved successfully!!!";
    }

    @Override
    public String updateMemberDetails(MemberDetailsDto memberDetailsDto, MultipartFile image) throws IOException {
        if(!repository.existsByOhrIdAndIsRegistrationDoneTrue(memberDetailsDto.getOhrId())) {
            throw new BadRequestException("Registration is not completed");
        }

        String ohrId = memberDetailsDto.getOhrId();
        Member memberDetailsFromDb = repository.findByOhrId(ohrId)
                .orElseThrow(() -> new BadRequestException("Member not found with OHR: " + ohrId));


        return "";
    }

    @Override
    public String savePassword(PasswordRequest passwordRequest) {

        String ohrId = passwordRequest.getOhrId();
        if (!CvsUtility.isValidOhrId(ohrId)) {
            throw new BadRequestException("OHR ID must be a 9-digit numeric value.");
        }

        if (!passwordRequest.getPassword().equals(passwordRequest.getConfirmationPassword())) {
            throw new BadRequestException("Password are not the same");
        }

        Member user = repository.findByOhrId(passwordRequest.getOhrId())
                .orElseThrow(() -> new BadRequestException("Member not found with OHR: " + ohrId));

        if (user.getIsInitialPasswordSet() != null && user.getIsInitialPasswordSet()) {
            throw new BadRequestException("Member has already set the initial password. Please log in and change your password.");
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        user.setIsInitialPasswordSet(true);

        repository.save(user);

        return "Password saved successfully!!!";
    }

    private Member saveMemberEntity(MemberDetailsDto memberDetails, MultipartFile image) throws IOException {

        String imageName = null;
        String imageType = null;
        byte[] data = null;

        if (image != null && StringUtils.isNotBlank(image.getOriginalFilename())) {
            imageName = image.getOriginalFilename();
            imageType = image.getContentType();
            data = image.getBytes();
        }

        return Member.builder()
                .firstName(memberDetails.getFirstName())
                .lastName(memberDetails.getLastName())
                .applicationArea(memberDetails.getApplicationArea())
                .tower(memberDetails.getTower())
                .reportingManager(memberDetails.getReportingManager())
                .genpactOnsiteSpoc(memberDetails.getGenpactOnsiteSpoc())
                .ohrId(memberDetails.getOhrId())
                .baseLocation(memberDetails.getBaseLocation())
                .primarySkill(memberDetails.getPrimarySkill())
                .currentWorkingSkills(memberDetails.getCurrentWorkingSkills())
                .designationBand(memberDetails.getDesignationBand())
                .cvsLead(memberDetails.getCvsLead())
                .clientManager(memberDetails.getClientManager())
                .zid(memberDetails.getZid())
                .overallExperience(memberDetails.getOverallExperience())
                .cvsExperience(memberDetails.getCvsExperience())
                .genpactExperience(memberDetails.getGenpactExperience())
                .technicalExpertise(memberDetails.getTechnicalExpertise())
                .contactNumber(memberDetails.getMobileNumber())
                .genpactMailId(memberDetails.getEmailId())
                .ssn(memberDetails.getSsn())
                .cvsEmpId(memberDetails.getCvsEmpId())
                .cvsMailId(memberDetails.getCvsMailId())
                .highestDegree(memberDetails.getHighestDegree())
                .birthday(memberDetails.getBirthday())
                .anniversary(memberDetails.getAnniversary())
                .currentAddress(memberDetails.getCurrentAddress())
                .emergencyContactName(memberDetails.getEmergencyContactName())
                .emergencyPhoneNumber(memberDetails.getEmergencyPhoneNumber())
                .role(Role.USER)
                .password(passwordEncoder.encode(memberDetails.getPassword()))
                .imageName(imageName)
                .imageType(imageType)
                .imageData(data)
                .isRegistrationDone(true)
                .seatNumber(memberDetails.getSeatNumber())
                .build();
    }

    private MemberDetailsDto memberDetailsBuilder(Member memberDetails) {
        return MemberDetailsDto.builder()
                .firstName(memberDetails.getFirstName())
                .lastName(memberDetails.getLastName())
                .applicationArea(memberDetails.getApplicationArea())
                .tower(memberDetails.getTower())
                .reportingManager(memberDetails.getReportingManager())
                .genpactOnsiteSpoc(memberDetails.getGenpactOnsiteSpoc())
                .ohrId(memberDetails.getOhrId())
                .baseLocation(memberDetails.getBaseLocation())
                .primarySkill(memberDetails.getPrimarySkill())
                .currentWorkingSkills(memberDetails.getCurrentWorkingSkills())
                .designationBand(memberDetails.getDesignationBand())
                .cvsLead(memberDetails.getCvsLead())
                .clientManager(memberDetails.getClientManager())
                .zid(memberDetails.getZid())
                .overallExperience(memberDetails.getOverallExperience())
                .cvsExperience(memberDetails.getCvsExperience())
                .genpactExperience(memberDetails.getGenpactExperience())
                .technicalExpertise(memberDetails.getTechnicalExpertise())
                .mobileNumber(memberDetails.getContactNumber())
                .emailId(memberDetails.getGenpactMailId())
                .ssn(memberDetails.getSsn())
                .cvsEmpId(memberDetails.getCvsEmpId())
                .highestDegree(memberDetails.getHighestDegree())
                .birthday(memberDetails.getBirthday())
                .anniversary(memberDetails.getAnniversary())
                .currentAddress(memberDetails.getCurrentAddress())
                .emergencyContactName(memberDetails.getEmergencyContactName())
                .emergencyPhoneNumber(memberDetails.getEmergencyPhoneNumber())
                .isRegistrationDone(memberDetails.getIsRegistrationDone())
                .build();
    }
}
