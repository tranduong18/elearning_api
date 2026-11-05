package com.duong.elearning_api.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CloudinaryUtils {
    // Regex bóc tách chuỗi nằm sau /upload/ (bỏ qua v123456/) đến trước phần mở rộng file (.jpg, .png...)
    private static final Pattern CLOUDINARY_PATTERN = Pattern.compile(".*/upload/(?:v\\d+/)?(.+?)(?:\\.[a-zA-Z0-9]+)?$");

    public static String extractPublicId(String url){
        if(url == null || url.isBlank()){
            return null;
        }

        Matcher matcher = CLOUDINARY_PATTERN.matcher(url);
        if(matcher.find()){
            return matcher.group(1);
        }

        return null;
    }
}
