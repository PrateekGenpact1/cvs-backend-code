package com.cvsnewsletter.scheduler;

import com.cvsnewsletter.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupScheduler {

    private final TokenRepository tokenRepository;

    @Scheduled(cron = "${scheduler.token-cleanup-cron}")
    @Transactional
    public void cleanUpOldTokens() {
        try {
            LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
            tokenRepository.deleteTokensOlderThan(cutoff);
            log.info("Old tokens deleted successfully at {}", LocalDateTime.now());
        } catch (Exception ex) {
            log.error("Error occurred while deleting old tokens: {}", ex.getMessage(), ex);
        }
    }
}
