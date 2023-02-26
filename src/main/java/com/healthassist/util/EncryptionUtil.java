package com.healthassist.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptionUtil {
	static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	public static String encryptPassword(String password) {

		String encryptedPassword = bCryptPasswordEncoder.encode(password);
		return encryptedPassword;
	}

	public static boolean isValidPassword(String password, String encryptedPassword) {
		// TODO Auto-generated method stub
		

		if (bCryptPasswordEncoder.matches(password, encryptedPassword)) {
			return true;
		}
		
		return false;
	}
	
}
