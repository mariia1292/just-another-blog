/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.cipher;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author sdorra
 */
public abstract class CipherTestBase
{

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract Cipher getCipher();

  //~--- methods --------------------------------------------------------------

  /**
   * Test of decode method, of class Cipher.
   */
  @Test
  public void testCipherBasics()
  {
    Cipher cipher = getCipher();

    assertNotNull(cipher);
    testEncodeDecode(cipher, null, "Hello World");
  }

  /**
   * Method description
   *
   */
  @Test
  public void testCipherCustomKey()
  {
    Cipher cipher = getCipher();

    assertNotNull(cipher);

    char[] key =
    {
      'c', 'h', 'e', 'e', 's', 'e'
    };

    cipher.setKey(key);
    testEncodeDecode(cipher, key, "Hello Cheese");
  }

  /**
   * Method description
   *
   *
   * @param cipher
   * @param key
   * @param value
   */
  private void testEncodeDecode(Cipher cipher, char[] key, String value)
  {
    String enc = null;

    if (key == null)
    {
      enc = cipher.encode(value);
    }
    else
    {
      enc = cipher.encode(key, value);
    }

    assertNotNull(enc);
    assertTrue(enc.length() > 0);
    assertFalse(value.equals(enc));

    String dec = null;

    if (key == null)
    {
      dec = cipher.decode(enc);
    }
    else
    {
      dec = cipher.decode(key, enc);
    }

    assertNotNull(dec);
    assertTrue(dec.length() > 0);
    assertEquals(value, dec);
  }
}
