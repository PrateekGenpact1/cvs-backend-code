package com.cvsnewsletter.services.implementation;

import com.cvsnewsletter.dtos.LimitedMemberDetailsDto;
import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.dtos.request.PasswordRequest;
import com.cvsnewsletter.dtos.response.MemberLocationResponse;
import com.cvsnewsletter.entities.Member;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LimitedMemberDetailsDto getMemberDetails(String ohrId) {

        if (!CvsUtility.isValidOhrId(ohrId)) {
            throw new BadRequestException("OHR ID must be a 9-digit numeric value.");
        }

        Member memberDetails = repository.findByOhrId(ohrId)
                .orElseThrow(() -> new BadRequestException("Member not found with OHR: " + ohrId));

        if (Boolean.TRUE.equals(memberDetails.getIsInitialPasswordSet())) {
            throw new BadRequestException("The member has already set their password. Please log in to view full details.");
        }

        return LimitedMemberDetailsDto.builder()
                .firstName(CvsUtility.getOrDefault(memberDetails.getFirstName()))
                .lastName(CvsUtility.getOrDefault(memberDetails.getLastName()))
                .ohrId(CvsUtility.getOrDefault(memberDetails.getOhrId()))
                .emailId(CvsUtility.getOrDefault(memberDetails.getGenpactMailId()))
                .mobileNumber(CvsUtility.getOrDefault(memberDetails.getContactNumber()))
                .role(memberDetails.getRole().name())
                .isInitialPasswordSet(CvsUtility.getOrDefault(memberDetails.getIsInitialPasswordSet()))
                .build();
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
            throw new BadRequestException("Registration has already been completed. You can proceed with editing your details.");
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

    @Override
    public MemberDetailsDto getFullMemberDetails(String ohrId) {
        if (!CvsUtility.isValidOhrId(ohrId)) {
            throw new BadRequestException("OHR ID must be a 9-digit numeric value.");
        }

        Member memberDetails = repository.findByOhrId(ohrId)
                .orElseThrow(() -> new BadRequestException("Member not found with OHR: " + ohrId));

        return memberDetailsBuilder(memberDetails);
    }

    @Override
    public List<MemberLocationResponse> getMembersByLocation(String location) {
        List<Member> members = repository.findByBaseLocation(location);

        return members.stream()
                .map(m -> MemberLocationResponse.builder()
                        .name(m.getFirstName() + " " + m.getLastName())
                        .ohrId(m.getOhrId())
                        .seatNumber(CvsUtility.getOrDefault(m.getSeatNumber()))
                        .build())
                .collect(Collectors.toList());
    }

    private Member saveMemberEntity(MemberDetailsDto memberDetails, MultipartFile image) throws IOException {

        Member member = repository.findByOhrId(memberDetails.getOhrId())
                .orElseThrow(() -> new BadRequestException("Member not found with OHR: " + memberDetails.getOhrId()));

        if(StringUtils.isNotBlank(memberDetails.getBirthday())) {
            if (CvsUtility.isValidDate(memberDetails.getBirthday())) {
                member.setBirthday(memberDetails.getBirthday());
            } else {
                throw new BadRequestException("Invalid birthday format. Expected dd-MM-yyyy.");
            }
        }

        if(StringUtils.isNotBlank(memberDetails.getAnniversary())) {
            if (CvsUtility.isValidDate(memberDetails.getAnniversary())) {
                member.setAnniversary(memberDetails.getAnniversary());
            } else {
                throw new BadRequestException("Invalid anniversary format. Expected dd-MM-yyyy.");
            }
        }

        if (image != null && StringUtils.isNotBlank(image.getOriginalFilename())) {
            member.setImageName(image.getOriginalFilename());
            member.setImageType(image.getContentType());
            member.setImageData(image.getBytes());
        }

        member.setApplicationArea(memberDetails.getApplicationArea());
        member.setTower(memberDetails.getTower());
        member.setReportingManager(memberDetails.getReportingManager());
        member.setGenpactOnsiteSpoc(memberDetails.getGenpactOnsiteSpoc());
        member.setBaseLocation(memberDetails.getBaseLocation());
        member.setPrimarySkill(String.join(",", memberDetails.getPrimarySkill()));
        member.setCurrentWorkingSkills(String.join(",", memberDetails.getCurrentWorkingSkills()));
        member.setDesignationBand(memberDetails.getDesignationBand());
        member.setCvsLead(memberDetails.getCvsLead());
        member.setClientManager(memberDetails.getClientManager());
        member.setZid(memberDetails.getZid());
        member.setOverallExperience(memberDetails.getOverallExperience());
        member.setCvsExperience(memberDetails.getCvsExperience());
        member.setGenpactExperience(memberDetails.getGenpactExperience());
        member.setTechnicalExpertise(memberDetails.getTechnicalExpertise());
        member.setSsn(memberDetails.getSsn());
        member.setCvsEmpId(memberDetails.getCvsEmpId());
        member.setCvsMailId(memberDetails.getCvsMailId());
        member.setHighestDegree(memberDetails.getHighestDegree());
        member.setBirthday(memberDetails.getBirthday());
        member.setAnniversary(memberDetails.getAnniversary());
        member.setCurrentAddress(memberDetails.getCurrentAddress());
        member.setEmergencyContactName(memberDetails.getEmergencyContactName());
        member.setEmergencyPhoneNumber(memberDetails.getEmergencyPhoneNumber());
        member.setSeatNumber(memberDetails.getSeatNumber());

        // Mark registration done
        member.setIsRegistrationDone(true);

        return member;
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
                .primarySkill(CvsUtility.safeSplitToList(memberDetails.getPrimarySkill()))
                .currentWorkingSkills(CvsUtility.safeSplitToList(memberDetails.getCurrentWorkingSkills()))
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
                .isInitialPasswordSet(memberDetails.getIsInitialPasswordSet())
                .build();
    }
}
