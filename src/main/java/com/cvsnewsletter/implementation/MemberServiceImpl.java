package com.cvsnewsletter.implementation;

import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.dtos.response.MemberResponse;
import com.cvsnewsletter.entities.Member;
import com.cvsnewsletter.exception.BadRequestionException;
import com.cvsnewsletter.repositories.MemberRepository;
import com.cvsnewsletter.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponse getMemberDetails(String ohrId) {
        Member memberDetails = repository.findByOhrId(ohrId)
                .orElseThrow(() -> new BadRequestionException("Member not found with OHR: " + ohrId));

        return MemberResponse.builder()
                .firstName(memberDetails.getFirstName())
                .lastName(memberDetails.getLastName())
                .applicationArea(memberDetails.getApplicationArea())
                .tower(memberDetails.getTower())
                .reportingManager(memberDetails.getReportingManager())
                .genpactOnsiteSpoc(memberDetails.getGenpactOnsiteSpoc())
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestionException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new BadRequestionException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }
}
