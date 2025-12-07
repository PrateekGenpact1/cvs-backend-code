package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.request.ChangePasswordRequest;

import java.security.Principal;

public interface UserService {
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
