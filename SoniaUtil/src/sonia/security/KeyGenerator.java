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

//~--- JDK imports ------------------------------------------------------------

import java.security.SecureRandom;

/**
 *
 * @author Sebastian Sdorra
 */
public class KeyGenerator
{

  /** Field description */
  private static final char CHARS[] =
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
    StringBuffer result = new StringBuffer();

    for (int i = 0; i < length; i++)
    {
      result.append(chars[random.nextInt(chars.length)]);
    }

    return result.toString();
  }
}