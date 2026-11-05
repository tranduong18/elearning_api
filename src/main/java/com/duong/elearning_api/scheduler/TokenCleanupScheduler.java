package com.duong.elearning_api.scheduler;

import com.duong.elearning_api.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "TOKEN-CLEANUP-SCHEDULER")
public class TokenCleanupScheduler {
    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens(){
        log.info("Bắt đầu quét và xóa các token hết hạn trong database...");

        Instant now = Instant.now();
        int deletedCount = tokenRepository.deleteExpiredTokens(now);

        log.info("Đã xóa thành công {} token hết hạn khỏi bảng tokens.", deletedCount);
    }
}
