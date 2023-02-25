package com.healthassist.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptionUtil {
    static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public static String encryptPassword(String password) {

        String encryptedPassword =  bCryptPasswordEncoder.encode(password);
        return encryptedPassword;
    }
}
