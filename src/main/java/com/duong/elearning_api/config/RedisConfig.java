package com.duong.elearning_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host:localhost}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private Integer port;

    @Value("${spring.data.redis.username:default}")
    private String username;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Value("${spring.data.redis.ssl.enabled:false}")
    private boolean sslEnabled;

    // Quản lý thông tin cấu hình và tạo kết nối (Non-blocking qua Netty) tới Redis Server
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
        if(username != null && !username.isEmpty()){
            redisConfig.setUsername(username);
        }
        if(password != null && !password.isEmpty()){
            redisConfig.setPassword(password);
        }

        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(2)) // mỗi lệnh Redis (GET, SET...) nếu quá 2 giây không có phản hồi thì ném exception. Tránh việc app bị treo vô hạn khi Redis chậm hoặc mất kết nối.
                .shutdownTimeout(Duration.ofMillis(100)); // khi tắt ứng dụng, chờ tối đa 100ms để đóng các tài nguyên của Lettuce (thread pool, event loop). Nhỏ thì app tắt nhanh.

        if (sslEnabled) {
            builder.useSsl(); // Bật TLS/SSL khi cần (ví dụ Redis trên cloud như AWS ElastiCache, Azure, Redis Cloud). Local thường tắt.
        }

        return new LettuceConnectionFactory(redisConfig, builder.build());
    }

    // Cung cấp công cụ thao tác dữ liệu (đọc/ghi) với Redis và tự động chuyển đổi Object sang JSON
    @Bean
    public RedisTemplate<String, Object> redisTemplate(JsonMapper jsonMapper){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());

        // Serialize Key và HashKey dạng String thân thiện
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Serialize Value và HashValue dạng JSON bằng JsonMapper của Spring Boot 4
        GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer(jsonMapper);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }

}
