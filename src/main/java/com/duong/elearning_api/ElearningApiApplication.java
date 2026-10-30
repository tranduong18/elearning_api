package com.duong.elearning_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ElearningApiApplication {
	static void main(String[] args) {
		SpringApplication.run(ElearningApiApplication.class, args);
	}
}
