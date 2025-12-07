package com.cvsnewsletter.implementation;

import com.cvsnewsletter.dtos.request.AuthenticationRequest;
import com.cvsnewsletter.dtos.response.AuthenticationResponse;
import com.cvsnewsletter.entities.Member;
import com.cvsnewsletter.entities.Token;
import com.cvsnewsletter.entities.enums.TokenType;
import com.cvsnewsletter.exception.BadRequestionException;
import com.cvsnewsletter.repositories.MemberRepository;
import com.cvsnewsletter.repositories.TokenRepository;
import com.cvsnewsletter.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final MemberRepository repository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getOhrId(),
                        request.getPassword()
                )
        );
        var user = repository.findByOhrId(request.getOhrId())
                .orElseThrow(() -> new BadRequestionException("User not found!!!"));
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private void saveUserToken(Member user, String jwtToken) {
        var token = Token.builder()
                .member(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Member user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
