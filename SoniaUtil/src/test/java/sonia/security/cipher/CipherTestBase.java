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



package sonia.security.cipher;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Sebastian Sdorra
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
