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



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.net.MalformedURLException;
import java.net.URL;

import java.text.SimpleDateFormat;

/**
 *
 * @author Sebastian Sdorra
 */
public class ValidateUtil
{

  /** Field description */
  private static final String REGEX_DOMAINNAME = "^[A-z0-9][\\w\\-\\.]+$";

  /** Field description */
  private static final String REGEX_MAIL =
    "^[A-z0-9][\\w.-]*@[A-z0-9][\\w\\-\\.]+\\.[A-z0-9]{2,6}$";

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param dateFormat
   *
   * @return
   */
  public static boolean isDateFormat(String dateFormat)
  {
    boolean result = false;

    try
    {
      new SimpleDateFormat(dateFormat);
      result = true;
    }
    catch (IllegalArgumentException ex) {}

    return result;
  }

  /**
   * Method description
   *
   *
   * @param domainName
   *
   * @return
   */
  public static boolean isDomainName(String domainName)
  {
    return Util.hasContent(domainName) && domainName.matches(REGEX_DOMAINNAME);
  }

  /**
   * Method description
   *
   *
   * @param mail
   *
   * @return
   */
  public static boolean isMail(String mail)
  {
    return Util.hasContent(mail) && mail.matches(REGEX_MAIL);
  }

  /**
   * Method description
   *
   *
   * @param url
   *
   * @return
   */
  public static boolean isUrl(String url)
  {
    boolean result = false;

    try
    {
      new URL(url);
      result = true;
    }
    catch (MalformedURLException ex) {}

    return result;
  }
}
