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



package sonia.web.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class WebUtil
{

  /** Field description */
  private static final String HEADER_ETAG = "Etag";

  /** Field description */
  private static final String HEADER_IFMS = "If-Modified-Since";

  /** Field description */
  private static final String HEADER_INM = "If-None-Match";

  /** Field description */
  private static final SimpleDateFormat HTTP_DATE_FORMAT =
    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

  /** Field description */
  private static Logger logger = Logger.getLogger(WebUtil.class.getName());

  //~--- static initializers --------------------------------------------------

  static
  {
    HTTP_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param response
   * @param file
   */
  public static void addETagHeader(HttpServletResponse response, File file)
  {
    response.addHeader(HEADER_ETAG, getETag(file));
  }

  /**
   * Method description
   *
   *
   * @param date
   *
   * @return
   */
  public static String formatHttpDate(Date date)
  {
    return HTTP_DATE_FORMAT.format(date);
  }

  /**
   * Method description
   *
   *
   * @param dateString
   *
   * @return
   *
   * @throws ParseException
   */
  public static Date parseHttpDate(String dateString) throws ParseException
  {
    return HTTP_DATE_FORMAT.parse(dateString);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   */
  public static String getETag(File file)
  {
    return new StringBuffer("W/\"").append(file.length()).append(
        file.lastModified()).append("\"").toString();
  }

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  public static Date getIfModifiedSinceDate(HttpServletRequest request)
  {
    Date date = null;
    String dateString = request.getHeader(HEADER_IFMS);

    if (Util.hasContent(dateString))
    {
      try
      {
        date = parseHttpDate(dateString);
      }
      catch (ParseException ex)
      {
        if (logger.isLoggable(Level.WARNING))
        {
          logger.log(Level.WARNING, null, ex);
        }
      }
    }

    return date;
  }

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  public static boolean isGzipSupported(HttpServletRequest request)
  {
    String enc = request.getHeader("Accept-Encoding");

    return (enc != null) && enc.contains("gzip");
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param file
   *
   * @return
   */
  public static boolean isModified(HttpServletRequest request, File file)
  {
    boolean result = true;
    Date modifiedSince = getIfModifiedSinceDate(request);

    if (modifiedSince != null)
    {
      if (modifiedSince.getTime() == file.lastModified())
      {
        result = false;
      }
    }

    if (result)
    {
      String inmEtag = request.getHeader(HEADER_INM);

      if (Util.hasContent(inmEtag) && inmEtag.equals(getETag(file)))
      {
        result = false;
      }
    }

    return result;
  }
}
