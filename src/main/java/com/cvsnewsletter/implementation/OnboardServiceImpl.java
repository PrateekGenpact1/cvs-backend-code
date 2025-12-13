package com.cvsnewsletter.implementation;

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

import java.util.EnumSet;

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

}
