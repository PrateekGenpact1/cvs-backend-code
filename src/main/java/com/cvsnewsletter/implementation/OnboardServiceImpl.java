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

        var user = Member.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .genpactMailId(request.getEmail())
                .ohrId(request.getOhrId())
                .role(Role.USER)
                .build();

        repository.save(user);

        return "User onboarded successfully!!!";
    }

    @Override
    @Transactional
    public void assignRoleToMember(String ohrId, Role newRole) {
        if (!EnumSet.of(Role.USER, Role.ADMIN, Role.MANAGER).contains(newRole)) {
            throw new BadRequestException("Invalid role: " + newRole);
        }

        Member member = repository.findByOhrId(ohrId)
                .orElseThrow(() -> new BadRequestException("Member not found"));

        member.setRole(newRole);
        repository.save(member);
    }

}
