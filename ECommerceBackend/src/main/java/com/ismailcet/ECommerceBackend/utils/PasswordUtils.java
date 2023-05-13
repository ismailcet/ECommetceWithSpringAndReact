package com.ismailcet.ECommerceBackend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtils {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password){
        return passwordEncoder.encode(password);
    }

    public static boolean verifyPassword(String hashPassword, String encodePassword){
        return passwordEncoder.matches(hashPassword, encodePassword);
    }

}
