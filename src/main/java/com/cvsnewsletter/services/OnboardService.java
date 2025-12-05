package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.request.OnboardRequest;
import com.cvsnewsletter.entities.enums.Role;

public interface OnboardService {
    String onboard(OnboardRequest request);

    void assignRoleToMember(String ohrId, Role newRole);
}
