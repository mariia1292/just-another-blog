/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.encryption;

//~--- JDK imports ------------------------------------------------------------

import java.security.MessageDigest;

/**
 *
 * @author sdorra
 */
public abstract class DigestEncryption implements Encryption
{

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract MessageDigest getMessageDigest();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param input
   *
   * @return
   */
  public String encrypt(String input)
  {
    if ((input == null) || (input.length() == 0))
    {
      throw new IllegalArgumentException(
          "String to encript cannot be null or zero length");
    }

    if (digest == null)
    {
      digest = getMessageDigest();
    }

    StringBuffer hexString = new StringBuffer();

    digest.update(input.getBytes());

    byte[] hash = digest.digest();

    for (int i = 0; i < hash.length; i++)
    {
      if ((0xff & hash[i]) < 0x10)
      {
        hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
      }
      else
      {
        hexString.append(Integer.toHexString(0xFF & hash[i]));
      }
    }

    return hexString.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected MessageDigest digest;
}
