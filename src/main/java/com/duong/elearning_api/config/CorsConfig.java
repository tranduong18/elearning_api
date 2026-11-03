package com.duong.elearning_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
    public static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",   // React / Next.js dev server
            "http://localhost:5173",   // Vite / Vue dev server
            "https://elearning.vn"     // Production domain
    );

    public static final List<String> ALLOWED_METHODS = List.of(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
    );

    private static final List<String> ALLOWED_HEADERS = List.of(
            "Authorization", "Content-Type", "Accept", "X-Requested-With", "Origin"
    );

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. Chỉ định rõ ràng các Domain Frontend được phép gọi API
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        // 2. Cho phép các HTTP Methods thông dụng
        configuration.setAllowedMethods(ALLOWED_METHODS);
        // 3. Cho phép các Headers cần thiết (đặc biệt là Authorization để gửi Bearer Token)
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        // 4. Cho phép gửi thông tin định danh (Cookies, HTTP Authentication)
        configuration.setAllowCredentials(true);
        // 5. Cache kết quả Preflight Request trong 1 giờ (3600 giây) để giảm tải
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
