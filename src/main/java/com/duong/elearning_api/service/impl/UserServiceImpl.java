package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.common.UserType;
import com.duong.elearning_api.dto.request.CreateUserRequest;
import com.duong.elearning_api.dto.response.CreateUserResponse;
import com.duong.elearning_api.dto.response.UserProfileResponse;
import com.duong.elearning_api.entity.Role;
import com.duong.elearning_api.entity.User;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.repository.RoleRepository;
import com.duong.elearning_api.repository.UserRepository;
import com.duong.elearning_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Role defaultRole = roleRepository.findByName(UserType.USER.name())
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(UserType.USER.name())
                        .description("Default role for users")
                        .build()));

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .phoneNumber(request.phoneNumber())
                .isActive(true)
                .build();

        user.addRole(defaultRole);
        userRepository.save(user);

        return CreateUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .isActive(user.getIsActive())
                .roles(Set.of(defaultRole.getName()))
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public UserProfileResponse getMyProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .isActive(user.getIsActive())
                .roles(roles)
                .build();
    }
}
