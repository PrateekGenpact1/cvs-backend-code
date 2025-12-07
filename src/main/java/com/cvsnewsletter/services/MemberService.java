package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.dtos.MemberDetailsDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface MemberService {
    MemberDetailsDto getMemberDetails(String ohrId);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);

    String saveMemberDetails(MemberDetailsDto memberDetails, MultipartFile image) throws IOException;
}
