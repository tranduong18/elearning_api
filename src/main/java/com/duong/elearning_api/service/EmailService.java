package com.duong.elearning_api.service;

import java.util.Map;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String content);

    void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables);
}
