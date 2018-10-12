package com.crispysnippets.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.crispysnippets.error.DigestProcessingException;

public class HashGenerator {
	/**
	   * Method to convert a byte array into an hexadecimal string.
	   * @param arrayBytes the array of bytes
	   * @return A hex string containing the converted array
	   */
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
    /**
	   * Method to convert a byte array into an hexadecimal string.
	   * @param arrayBytes the array of bytes
	   * @return A hex string containing the converted array
	   */
    private static String hashString(String message, String algorithm)
            throws DigestProcessingException {
     
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));
     
            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new DigestProcessingException(
                    "Could not generate hash from String", ex);
        }
    }
}
