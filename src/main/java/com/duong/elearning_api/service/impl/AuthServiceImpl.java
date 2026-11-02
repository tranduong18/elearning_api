package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.common.TokenType;
import com.duong.elearning_api.dto.request.*;
import com.duong.elearning_api.dto.response.IntrospectResponse;
import com.duong.elearning_api.dto.response.LoginResponse;
import com.duong.elearning_api.entity.Token;
import com.duong.elearning_api.entity.User;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.repository.TokenRepository;
import com.duong.elearning_api.repository.UserRepository;
import com.duong.elearning_api.security.JwtService;
import com.duong.elearning_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTH-SERVICE")
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = (User) authentication.getPrincipal();

        if(user == null){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        Set<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String accessToken = jwtService.generateAccessToken(user.getId(), authorities);
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        Token token = Token.builder()
                .token(refreshToken)
                .tokenType(TokenType.REFRESH_TOKEN)
                .user(user)
                .expiryDate(Instant.now().plus(14, ChronoUnit.DAYS))
                .revoked(false)
                .build();
        tokenRepository.save(token);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .roles(authorities)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        return jwtService.introspectToken(request.token());
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        Token token = tokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // Kiểm tra xem token đã bị thu hồi hoặc quá hạn chưa
        if(token.getRevoked() || token.getExpiryDate().isBefore(Instant.now())){
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        User user = token.getUser();
        Set<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String newAccessToken = jwtService.generateAccessToken(user.getId(), authorities);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.refreshToken())
                .roles(authorities)
                .build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        Token token = tokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        token.setRevoked(true);
        tokenRepository.save(token);
    }

    @Override
    @Transactional
    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 1. Kiểm tra mật khẩu cũ có đúng không
        if(!passwordEncoder.matches(request.oldPassword(), user.getPassword())){
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        // 2. Không cho phép đặt mật khẩu mới trùng mật khẩu cũ
        if(passwordEncoder.matches(request.newPassword(), user.getPassword())){
            throw new AppException(ErrorCode.SAME_OLD_PASSWORD);
        }

        // 3. Mã hóa mật khẩu mới và lưu vào database
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        // 4. Thu hồi toàn bộ Refresh Token cũ của người dùng này để bắt đăng nhập lại
        tokenRepository.deleteByUser(user);
    }
}
