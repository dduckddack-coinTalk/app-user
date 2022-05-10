package com.cointalk.user.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    public static String encrypt(String text) {
        StringBuffer hexString = new StringBuffer();

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException error) {
            error.printStackTrace();
        }

        return hexString.toString().toUpperCase();
    }
}