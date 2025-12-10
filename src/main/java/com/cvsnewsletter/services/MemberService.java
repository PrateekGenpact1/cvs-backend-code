package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.PasswordRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface MemberService {
    MemberDetailsDto getMemberDetails(String ohrId);

    String changePassword(ChangePasswordRequest request, Principal connectedUser);

    String saveMemberDetails(MemberDetailsDto memberDetails, MultipartFile image) throws IOException;

    String updateMemberDetails(MemberDetailsDto memberDetailsDto, MultipartFile image) throws IOException;

    String savePassword(PasswordRequest passwordRequest);
}
