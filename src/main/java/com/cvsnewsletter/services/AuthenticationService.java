package com.cvsnewsletter.services;

import com.cvsnewsletter.dtos.request.AuthenticationRequest;
import com.cvsnewsletter.dtos.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
