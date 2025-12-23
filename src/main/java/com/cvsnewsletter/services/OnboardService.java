package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.MemberDetailsDto;
import com.cvsnewsletter.dtos.request.OnboardRequest;
import com.cvsnewsletter.entities.enums.Role;

import java.util.List;

public interface OnboardService {
    String onboard(OnboardRequest request);

    void assignRoleToMember(String ohrId, String roleStr);

    List<MemberDetailsDto> getAllMemberDetails();

    String updateInitialPasswordFlag(String ohrId);
}
