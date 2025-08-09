package com.govind.ecommerce.data;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * This class is used to generate a secret key based on HmacSHA256 algorithm
 */
public class GenerateKey {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGenerator.generateKey();
        String jwtSecret = Base64.getUrlEncoder().withoutPadding().encodeToString(sk.getEncoded()); // Standard Base64
        System.out.println(jwtSecret);
    }
}
