package com.cvsnewsletter.services.implementation;

import com.cvsnewsletter.dtos.LimitedMemberDetailsDto;
import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.dtos.request.PasswordRequest;
import com.cvsnewsletter.dtos.response.MemberLocationResponse;
import com.cvsnewsletter.dtos.response.MemberSummaryResponse;
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
    public LimitedMemberDetailsDto getMemberDetails(String ohrId, String emergencyPhoneNumber) {

        if (!CvsUtility.isValidOhrId(ohrId)) {
            throw new BadRequestException("OHR ID must be a 9-digit numeric value.");
        }

        if (!CvsUtility.isValidMobileNumber(emergencyPhoneNumber)) {
            throw new BadRequestException("Invalid mobile number.");
        }

        Member memberDetails = repository.findByOhrIdAndEmergencyPhoneNumber(ohrId, emergencyPhoneNumber)
                .orElseThrow(() -> new BadRequestException(
                        String.format("Member not found with OHR ID: %s and Emergency Phone Number: %s", ohrId, emergencyPhoneNumber)
                ));

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
                .emergencyContactName(CvsUtility.getOrDefault(memberDetails.getEmergencyContactName()))
                .emergencyPhoneNumber(CvsUtility.getOrDefault(memberDetails.getEmergencyPhoneNumber()))
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
    public String updateMemberDetails(MemberDetailsDto dto, MultipartFile image) throws IOException {
        if (!repository.existsByOhrIdAndIsRegistrationDoneTrue(dto.getOhrId())) {
            throw new BadRequestException("You haven't completed the registration, first complete the registration then proceed with edit.");
        }

        Member member = repository.findByOhrId(dto.getOhrId())
                .orElseThrow(() -> new BadRequestException("Member not found with OHR: " + dto.getOhrId()));

        member.setFirstName(updateIfNotBlank(dto.getFirstName(), member.getFirstName()));
        member.setLastName(updateIfNotBlank(dto.getLastName(), member.getLastName()));
        member.setContactNumber(updateIfNotBlank(dto.getMobileNumber(), member.getContactNumber()));
        member.setCvsMailId(updateIfNotBlank(dto.getEmailId(), member.getCvsMailId()));
        member.setApplicationArea(updateIfNotBlank(dto.getApplicationArea(), member.getApplicationArea()));
        member.setTower(updateIfNotBlank(dto.getTower(), member.getTower()));
        member.setReportingManager(updateIfNotBlank(dto.getReportingManager(), member.getReportingManager()));
        member.setGenpactOnsiteSpoc(updateIfNotBlank(dto.getGenpactOnsiteSpoc(), member.getGenpactOnsiteSpoc()));
        member.setBaseLocation(updateIfNotBlank(dto.getBaseLocation(), member.getBaseLocation()));
        member.setDesignationBand(updateIfNotBlank(dto.getDesignationBand(), member.getDesignationBand()));
        member.setCvsLead(updateIfNotBlank(dto.getCvsLead(), member.getCvsLead()));
        member.setClientManager(updateIfNotBlank(dto.getClientManager(), member.getClientManager()));
        member.setZid(updateIfNotBlank(dto.getZid(), member.getZid()));
        member.setOverallExperience(updateIfNotBlank(dto.getOverallExperience(), member.getOverallExperience()));
        member.setCvsExperience(updateIfNotBlank(dto.getCvsExperience(), member.getCvsExperience()));
        member.setGenpactExperience(updateIfNotBlank(dto.getGenpactExperience(), member.getGenpactExperience()));
        member.setTechnicalExpertise(updateIfNotBlank(dto.getTechnicalExpertise(), member.getTechnicalExpertise()));
        member.setSsn(updateIfNotBlank(dto.getSsn(), member.getSsn()));
        member.setCvsEmpId(updateIfNotBlank(dto.getCvsEmpId(), member.getCvsEmpId()));
        member.setCvsMailId(updateIfNotBlank(dto.getCvsMailId(), member.getCvsMailId()));
        member.setHighestDegree(updateIfNotBlank(dto.getHighestDegree(), member.getHighestDegree()));
        member.setCurrentAddress(updateIfNotBlank(dto.getCurrentAddress(), member.getCurrentAddress()));
        member.setEmergencyContactName(updateIfNotBlank(dto.getEmergencyContactName(), member.getEmergencyContactName()));
        member.setEmergencyPhoneNumber(updateIfNotBlank(dto.getEmergencyPhoneNumber(), member.getEmergencyPhoneNumber()));
        member.setSeatNumber(updateIfNotBlank(dto.getSeatNumber(), member.getSeatNumber()));
        member.setPrimarySkill(updateListField(dto.getPrimarySkill(), member.getPrimarySkill()));
        member.setCurrentWorkingSkills(updateListField(dto.getCurrentWorkingSkills(), member.getCurrentWorkingSkills()));

        if (StringUtils.isNotBlank(dto.getBirthday())) {
            if (CvsUtility.isValidDate(dto.getBirthday())) {
                member.setBirthday(dto.getBirthday());
            } else {
                throw new BadRequestException("Invalid birthday format. Expected dd-MM-yyyy.");
            }
        }

        if (StringUtils.isNotBlank(dto.getAnniversary())) {
            if (CvsUtility.isValidDate(dto.getAnniversary())) {
                member.setAnniversary(dto.getAnniversary());
            } else {
                throw new BadRequestException("Invalid anniversary format. Expected dd-MM-yyyy.");
            }
        }

        if (image != null && StringUtils.isNotBlank(image.getOriginalFilename())) {
            if (!CvsUtility.isImageFile(image)) {
                throw new BadRequestException("Invalid file type. Only image files are allowed.");
            }
            member.setImageName(image.getOriginalFilename());
            member.setImageType(image.getContentType());
            member.setImageData(image.getBytes());
        }

        repository.save(member);
        return "Member details updated successfully.";
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

    @Override
    public List<MemberSummaryResponse> getAllMembersSummary() {
        return repository.findAll().stream()
                .map(member -> MemberSummaryResponse.builder()
                        .firstName(CvsUtility.getOrDefault(member.getFirstName()))
                        .lastName(CvsUtility.getOrDefault(member.getLastName()))
                        .applicationArea(CvsUtility.getOrDefault(member.getApplicationArea()))
                        .tower(CvsUtility.getOrDefault(member.getTower()))
                        .reportingManager(CvsUtility.getOrDefault(member.getReportingManager()))
                        .birthday(CvsUtility.getOrDefault(member.getBirthday()))
                        .anniversary(CvsUtility.getOrDefault(member.getAnniversary()))
                        .baseLocation(CvsUtility.getOrDefault(member.getBaseLocation()))
                        .seatNumber(CvsUtility.getOrDefault(member.getSeatNumber()))
                        .build()
                )
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

    private String updateIfNotBlank(String newValue, String oldValue) {
        return StringUtils.isNotBlank(newValue) ? newValue : oldValue;
    }

    private String updateListField(List<String> newList, String oldValue) {
        if (newList == null || newList.isEmpty()) {
            return oldValue;
        }
        return String.join(",", newList);
    }

}
