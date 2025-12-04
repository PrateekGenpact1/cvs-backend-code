package com.cvsnewsletter.implementation;

import com.cvsnewsletter.dtos.request.OnboardRequest;
import com.cvsnewsletter.entities.Member;
import com.cvsnewsletter.entities.enums.Role;
import com.cvsnewsletter.repositories.MemberRepository;
import com.cvsnewsletter.services.OnboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardServiceImpl implements OnboardService {

    private final MemberRepository repository;

    @Override
    public String onboard(OnboardRequest request) {
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

}
