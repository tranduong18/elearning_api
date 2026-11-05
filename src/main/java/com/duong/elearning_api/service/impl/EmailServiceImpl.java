package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j(topic = "EMAIL-SERVICE")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            log.info("Send email to {} successfully", to);
        } catch (MailException e) {
            log.error("Error sending email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            // 1. Chuẩn bị ngữ cảnh dữ liệu Thymeleaf Context
            Context context = new Context();
            context.setVariables(variables);

            // 2. Biên dịch template HTML thành chuỗi String đã thế dữ liệu
            String htmlContent = templateEngine.process(templateName, context);
            // 3. Đóng gói vào MimeMessage hỗ trợ UTF-8 và HTML
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // 4. Phát lệnh gửi qua SMTP
            mailSender.send(mimeMessage);
            log.info("Sending HTML email to {} successfully", to);

        } catch (MessagingException e) {
            log.error("Error send HTMl email: {}", e.getMessage());
        }
    }
}
