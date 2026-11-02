package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.Token;
import com.duong.elearning_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);

    void deleteByUser(User user);
}
