package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.request.ChangePasswordRequest;
import com.cvsnewsletter.dtos.response.MemberResponse;

import java.security.Principal;

public interface MemberService {
    MemberResponse getMemberDetails(String ohrId);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
