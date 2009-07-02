/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author sdorra
 */
public class KeyGeneratorTest
{

  /**
   * Test of generateKey method, of class KeyGenerator.
   */
  @Test
  public void testGenerateKey()
  {
    String key1 = KeyGenerator.generateKey();
    String key2 = KeyGenerator.generateKey();
    String key3 = KeyGenerator.generateKey();
    String key4 = KeyGenerator.generateKey();

    assertNotNull(key1);
    assertNotNull(key2);
    assertNotNull(key3);
    assertNotNull(key4);
  }

  /**
   * Method description
   *
   */
  @Test
  public void testGenerateKeyChars()
  {
    char[] chars =
    {
      'a', 'b', 'c', 'd', 'e', 'f', '1', '2', '3', '4', '5', '6'
    };
    String key = KeyGenerator.generateKey(12, chars);

    assertNotNull(key);
    assertTrue(key.length() == 12);

    for (char c : key.toCharArray())
    {
      assertTrue(isIn(chars, c));
    }
  }

  /**
   * Method description
   *
   */
  @Test
  public void testGenerateKeyLength()
  {
    String key1 = KeyGenerator.generateKey(1);
    String key2 = KeyGenerator.generateKey(2);
    String key3 = KeyGenerator.generateKey(3);
    String key4 = KeyGenerator.generateKey(4);

    assertNotNull(key1);
    assertNotNull(key2);
    assertNotNull(key3);
    assertNotNull(key4);
    assertTrue(key1.length() == 1);
    assertTrue(key2.length() == 2);
    assertTrue(key3.length() == 3);
    assertTrue(key4.length() == 4);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param chars
   * @param c
   *
   * @return
   */
  private boolean isIn(char[] chars, char c)
  {
    boolean result = false;

    for (char oc : chars)
    {
      if (oc == c)
      {
        result = true;

        break;
      }
    }

    return result;
  }
}
