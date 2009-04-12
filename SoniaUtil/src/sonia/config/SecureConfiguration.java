/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- non-JDK imports --------------------------------------------------------

import sonia.security.cipher.Cipher;

/**
 *
 * @author sdorra
 */
public interface SecureConfiguration extends ModifyableConfiguration
{

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String getSecureString(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public String getSecureString(String key, String def);

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cipher
   */
  public void setCipher(Cipher cipher);

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   */
  public void setSecureString(String key, String value);
}
