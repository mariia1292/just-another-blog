/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.cipher;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Convert;

//~--- JDK imports ------------------------------------------------------------

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 *
 * @author sdorra
 */
public class DefaultCipher extends Cipher
{

  /** Field description */
  private static final String CIPHER_NAME = "PBEWithMD5AndDES";

  /** Field description */
  private static SecureRandom random = new SecureRandom();

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultCipher.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DefaultCipher()
  {
    super();
  }

  /**
   * Constructs ...
   *
   *
   * @param key
   */
  public DefaultCipher(char[] key)
  {
    super(key);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param secretKey
   *
   * @return
   *
   * @throws InvalidKeySpecException
   * @throws NoSuchAlgorithmException
   */
  private static SecretKey buildSecretKey(char[] secretKey)
          throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    PBEKeySpec keySpec = new PBEKeySpec(secretKey);
    SecretKeyFactory factory = SecretKeyFactory.getInstance(CIPHER_NAME);

    return factory.generateSecret(keySpec);
  }

  /**
   * Method description
   *
   *
   * @param secretKey
   * @param value
   *
   * @return
   */
  public String decode(char[] secretKey, String value)
  {
    String result = null;

    try
    {
      byte[] encodedInput = Convert.fromBase64(value);
      byte[] salt = new byte[8];
      byte[] encoded = new byte[encodedInput.length - 8];

      System.arraycopy(encodedInput, 0, salt, 0, 8);
      System.arraycopy(encodedInput, 8, encoded, 0, encodedInput.length - 8);

      PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 20);
      SecretKey key = buildSecretKey(secretKey);
      javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_NAME);

      cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, parameterSpec);

      byte[] deoded = cipher.doFinal(encoded);

      result = new String(deoded, "UTF-8");
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);

      throw new CipherException(ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param secretKey
   * @param value
   *
   * @return
   */
  public String encode(char[] secretKey, String value)
  {
    String res = null;

    try
    {
      byte[] salt = new byte[8];

      random.nextBytes(salt);

      PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 20);
      SecretKey key = buildSecretKey(secretKey);
      javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER_NAME);

      cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, parameterSpec);

      byte[] inputBytes = value.getBytes("UTF-8");
      byte[] encodedInput = cipher.doFinal(inputBytes);
      byte[] result = new byte[salt.length + encodedInput.length];

      System.arraycopy(salt, 0, result, 0, 8);
      System.arraycopy(encodedInput, 0, result, 8, result.length - 8);
      res = Convert.toBase64(result);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);

      throw new CipherException(ex);
    }

    return res;
  }
}
