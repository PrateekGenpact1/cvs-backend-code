package com.cvsnewsletter.services.implementation;

import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.OnboardRequest;
import com.cvsnewsletter.entities.Member;
import com.cvsnewsletter.entities.enums.Role;
import com.cvsnewsletter.exception.BadRequestException;
import com.cvsnewsletter.repositories.MemberRepository;
import com.cvsnewsletter.services.OnboardService;
import com.cvsnewsletter.utility.CvsUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OnboardServiceImpl implements OnboardService {

    private final MemberRepository repository;

    @Override
    public String onboard(OnboardRequest request) {
        if (!CvsUtility.isValidOhrId(request.getOhrId())) {
            throw new BadRequestException("OHR ID must be a 9-digit numeric value.");
        }

        if (!CvsUtility.isValidMobileNumber(request.getMobileNumber())) {
            throw new BadRequestException("Invalid mobile number.");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + request.getRole());
        }

        if (!EnumSet.of(Role.USER, Role.ADMIN, Role.MANAGER).contains(role)) {
            throw new BadRequestException("Invalid role: " + role);
        }

        var user = Member.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .genpactMailId(request.getEmail())
                .ohrId(request.getOhrId())
                .contactNumber(request.getMobileNumber())
                .role(role)
                .build();

        repository.save(user);

        return "User onboarded successfully!!!";
    }

    @Override
    @Transactional
    public void assignRoleToMember(String ohrId, String roleStr) {
        if (!CvsUtility.isValidOhrId(ohrId)) {
            throw new BadRequestException("OHR ID must be a 9-digit numeric value.");
        }

        Role newRole;
        try {
            newRole = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + roleStr);
        }

        if (!EnumSet.of(Role.USER, Role.ADMIN, Role.MANAGER).contains(newRole)) {
            throw new BadRequestException("Invalid role: " + newRole);
        }

        Member member = repository.findByOhrId(ohrId)
                .orElseThrow(() -> new BadRequestException("Member not found"));

        member.setRole(newRole);
        repository.save(member);
    }

    @Override
    public List<MemberDetailsDto> getAllMemberDetails() {
        List<Member> allMemberDetails = repository.findAll();

        List<MemberDetailsDto> memberList = new ArrayList<>();
        for(Member member : allMemberDetails) {
            memberList.add(this.memberDetailsBuilder(member));
        }

        return memberList;
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
