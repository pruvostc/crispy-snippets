package com.crispysnippets.security;

import com.crispysnippets.error.DigestProcessingException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/** ThisClass generate the Hash value for the digital signature. **/
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
   * @return A hex string containing the converted array
   * @throws DigestProcessingException exception
   * {@value #algorithm} String SHA-256 MD5 SHA-1
   */
  public static String hashString(String message, String algorithm)
            throws DigestProcessingException {
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      md.update(message.getBytes("UTF-8"));
      byte[] hashedBytes = md.digest();
      return convertByteArrayToHexString(hashedBytes);     
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
      throw new DigestProcessingException("Hash Generation Error", ex);
    }
  }
}
