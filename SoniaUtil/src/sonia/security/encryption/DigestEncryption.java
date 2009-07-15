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


package sonia.security.encryption;

//~--- JDK imports ------------------------------------------------------------

import java.security.MessageDigest;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class DigestEncryption implements Encryption
{

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract MessageDigest getMessageDigest();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param input
   *
   * @return
   */
  public String encrypt(String input)
  {
    if ((input == null) || (input.length() == 0))
    {
      throw new IllegalArgumentException(
          "String to encript cannot be null or zero length");
    }

    if (digest == null)
    {
      digest = getMessageDigest();
    }

    StringBuffer hexString = new StringBuffer();

    digest.update(input.getBytes());

    byte[] hash = digest.digest();

    for (int i = 0; i < hash.length; i++)
    {
      if ((0xff & hash[i]) < 0x10)
      {
        hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
      }
      else
      {
        hexString.append(Integer.toHexString(0xFF & hash[i]));
      }
    }

    return hexString.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected MessageDigest digest;
}