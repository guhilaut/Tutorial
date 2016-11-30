package com.cox.util;

import org.jasypt.util.text.BasicTextEncryptor;

public class DecryptUtil {
	
	private static String salt = "XIS-IND-327";
	
	public static String decrypt(String encryptedPassword){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(salt);
        String decryptedPassword = textEncryptor.decrypt(encryptedPassword);
        return decryptedPassword;
	}

	public static void main(String[] args) {
		System.out.println(decrypt("ugEBH1XqrNNgJbp9kSI4sQ=="));
	}
}
