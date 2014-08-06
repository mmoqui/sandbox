package org.silverpeas.sandbox.jee7test.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;

/**
 * @author mmoquillon
 */
public class PasswordEncryption {

  private static Key key;

  static {
    KeyGenerator generator = null;
    try {
      generator = KeyGenerator.getInstance("AES", "BC");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    }
    generator.init(256);
    key = generator.generateKey();
  }

  public String encrypt(final String password)
      throws GeneralSecurityException {
    SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
    cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keySpec);
    byte[] cipherText = cipher.doFinal(password.getBytes(Charset.forName("UTF-8")));
    return Base64.getEncoder().encodeToString(cipherText);
  }
}
