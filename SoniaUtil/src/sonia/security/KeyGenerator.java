/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security;

//~--- JDK imports ------------------------------------------------------------

import java.security.SecureRandom;

/**
 *
 * @author sdorra
 */
public class KeyGenerator
{

  /** Field description */
  public static final char CHARS[] =
  {
    'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', 'g', 'G', 'h',
    'H', 'k', 'K', 'm', 'M', 'n', 'N', 'p', 'P', 'r', 'R', 's', 'S', 't', 'T',
    'u', 'U', 'w', 'W', 'x', 'X', 'y', 'Y', 'z', 'Z', '0', '1', '2', '3', '4',
    '5', '6', '7', '8', '9'
  };

  /** Field description */
  private static SecureRandom random = new SecureRandom();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static String generateKey()
  {
    return generateKey(8, CHARS);
  }

  /**
   * Method description
   *
   *
   *
   * @param length
   * @return
   */
  public static String generateKey(int length)
  {
    return generateKey(length, CHARS);
  }

  /**
   * Method description
   *
   *
   * @param length
   * @param chars
   *
   * @return
   */
  public static String generateKey(int length, char[] chars)
  {
    String result = "";

    for (int i = 0; i < length; i++)
    {
      result += chars[random.nextInt(chars.length)];
    }

    return result;
  }
}
