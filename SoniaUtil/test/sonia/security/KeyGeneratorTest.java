/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
 */


package sonia.security;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Sebastian Sdorra
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