package com.mentorshipApi.mentorship.utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encryptPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}