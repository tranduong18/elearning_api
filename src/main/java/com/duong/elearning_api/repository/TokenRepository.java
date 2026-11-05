package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.Token;
import com.duong.elearning_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);

    void deleteByUser(User user);

    // Xóa tất cả các token có hạn sử dụng nhỏ hơn thời điểm hiện tại
    @Modifying
    @Transactional
    @Query("delete from Token t where t.expiryDate < :now")
    int deleteExpiredTokens(@Param("now") Instant now);
}
