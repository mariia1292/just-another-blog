/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.encryption;

//~--- JDK imports ------------------------------------------------------------

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author sdorra
 */
public class MD5Encryption extends DigestEncryption
{

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected MessageDigest getMessageDigest()
  {
    try
    {
      return MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException ex)
    {

      // should not happen
      throw new RuntimeException(ex);
    }
  }
}
