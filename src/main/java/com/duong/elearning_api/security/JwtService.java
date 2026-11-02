package com.duong.elearning_api.security;

import com.duong.elearning_api.common.TokenType;
import com.duong.elearning_api.dto.response.IntrospectResponse;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
@Slf4j(topic = "JWT-SERVICE")
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-seconds}")
    private long accessTokenExpirationSeconds;

    @Value("${jwt.refresh-token-expiration-seconds}")
    private long refreshTokenExpirationSeconds;

    public String generateAccessToken(String userId, Set<String> authorities){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Date issueTime = new Date();
        Date expirationTime = Date.from(issueTime.toInstant().plusSeconds(accessTokenExpirationSeconds));
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .jwtID(jwtId)
                .claim("type", TokenType.ACCESS_TOKEN.name())
                .claim("authorities", authorities)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);
        try {
            signedJWT.sign(new MACSigner(secretKey));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error generating access token: {}", e.getMessage());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public String generateRefreshToken(String userId){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Date issueTime = new Date();
        Date expirationTime = Date.from(issueTime.toInstant().plusSeconds(refreshTokenExpirationSeconds));
        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .jwtID(jwtId)
                .claim("type", TokenType.REFRESH_TOKEN.name())
                .build();

        SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);
        try {
            signedJWT.sign(new MACSigner(secretKey));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error generating refresh token: {}", e.getMessage());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public IntrospectResponse introspectToken(String token){
        try {
            // 1. Phân tích chuỗi JWT
            SignedJWT signedJWT = SignedJWT.parse(token);

            // 2. Kiểm tra chữ ký bằng MACVerifier với Secret Key
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            boolean isValid = signedJWT.verify(verifier);

            if(!isValid){
                log.warn("Invalid token: {}", token);
                return IntrospectResponse.builder()
                        .isValid(false)
                        .roles(Set.of())
                        .build();
            }

            // 3. Kiểm tra thời hạn hết hạn (Expiration Time)
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean isExpired = expirationTime.before(new Date());
            if(isExpired){
                log.warn("Token has expired: {}", token);
                return IntrospectResponse.builder()
                        .isValid(false)
                        .roles(Set.of())
                        .build();
            }

            // 4. Trích xuất danh sách quyền từ claim "authorities"
            List<String> authorities = signedJWT.getJWTClaimsSet().getStringListClaim("authorities");
            Set<String> roles = authorities != null ? new HashSet<>(authorities) : Set.of();
            return IntrospectResponse.builder()
                    .isValid(true)
                    .roles(roles)
                    .build();

        } catch (ParseException | JOSEException e) {
            log.error("Error parsing token: {}", e.getMessage());
            return IntrospectResponse.builder()
                    .isValid(false)
                    .roles(Set.of())
                    .build();
        }
    }
}
