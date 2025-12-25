package com.cvsnewsletter.services.implementation;

import com.cvsnewsletter.config.LockoutProperties;
import com.cvsnewsletter.dtos.request.AuthenticationRequest;
import com.cvsnewsletter.dtos.response.AuthenticationResponse;
import com.cvsnewsletter.entities.Member;
import com.cvsnewsletter.entities.Token;
import com.cvsnewsletter.entities.enums.TokenType;
import com.cvsnewsletter.exception.BadRequestException;
import com.cvsnewsletter.repositories.MemberRepository;
import com.cvsnewsletter.repositories.TokenRepository;
import com.cvsnewsletter.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final MemberRepository repository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LockoutProperties lockoutProperties;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = repository.findByOhrId(request.getOhrId())
                .orElseThrow(() -> new BadRequestException("User not found!!!"));

        if (user.getIncorrectPasswordCount() >= lockoutProperties.getMaxAttempts()
                && user.getLastIncorrectPasswordTimestamp() != null) {
            LocalDateTime lockoutEnd = user.getLastIncorrectPasswordTimestamp()
                    .plusMinutes(lockoutProperties.getDurationMinutes());
            if (LocalDateTime.now().isBefore(lockoutEnd)) {
                throw new BadRequestException("Account locked. Try again after " + lockoutProperties.getDurationMinutes() + " minutes.");
            } else {
                user.setIncorrectPasswordCount(0);
                user.setLastIncorrectPasswordTimestamp(null);
                repository.save(user);
            }
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getOhrId(),
                            request.getPassword()
                    )
            );
        } catch (Exception ex) {
            user.setIncorrectPasswordCount(user.getIncorrectPasswordCount() + 1);
            user.setLastIncorrectPasswordTimestamp(LocalDateTime.now());
            repository.save(user);
            throw new BadRequestException("Invalid credentials. Attempt " + user.getIncorrectPasswordCount() + "/" + lockoutProperties.getMaxAttempts());
        }

        // Successful login → reset counters
        user.setIncorrectPasswordCount(0);
        user.setLastIncorrectPasswordTimestamp(null);
        repository.save(user);

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
                .createdAt(LocalDateTime.now())
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
