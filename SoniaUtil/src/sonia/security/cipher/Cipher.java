/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.cipher;

/**
 *
 * @author sdorra
 */
public interface Cipher
{

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   *
   * @return
   */
  public String decode(char[] key, String value);

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   *
   * @return
   */
  public String encode(char[] key, String value);
}
