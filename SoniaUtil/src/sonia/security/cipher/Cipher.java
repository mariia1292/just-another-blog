/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.cipher;

//~--- non-JDK imports --------------------------------------------------------

import sonia.security.KeyGenerator;

/**
 *
 * @author sdorra
 */
public abstract class Cipher
{

  /**
   * Constructs ...
   *
   */
  public Cipher()
  {
    this.key = KeyGenerator.generateKey(16).toCharArray();
  }

  /**
   * Constructs ...
   *
   *
   * @param key
   */
  public Cipher(char[] key)
  {
    this.key = key;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   *
   * @return
   */
  public abstract String decode(char[] key, String value);

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   *
   * @return
   */
  public abstract String encode(char[] key, String value);

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  public String decode(String value)
  {
    return decode(key, value);
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  public String encode(String value)
  {
    return encode(key, value);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   */
  public void setKey(char[] key)
  {
    this.key = key;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private char[] key;
}
