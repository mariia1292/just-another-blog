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

import sonia.web.Resource;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.text.DateFormat;
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
  public static final String DEFAULT_CONTENTTYPE = "application/octet-stream";

  /** Field description */
  public static final String HEADER_CACHECONTROL = "Cache-Control";

  /** Field description */
  public static final String HEADER_ETAG = "Etag";

  /** Field description */
  public static final String HEADER_EXPIRES = "Expires";

  /** Field description */
  public static final String HEADER_IFMS = "If-Modified-Since";

  /** Field description */
  public static final String HEADER_INM = "If-None-Match";

  /** Field description */
  public static final String HEADER_LASTMODIFIED = "Last-Modified";

  /** Field description */
  public static final String SCHEME_HTTPS = "https";

  /** Field description */
  public static final long TIME_MONTH = 60 * 60 * 24 * 31;

  /** Field description */
  public static final long TIME_YEAR = 60 * 60 * 24 * 365;

  /** Field description */
  private static final String HTTP_DATE_FORMAT =
    "EEE, dd MMM yyyy HH:mm:ss zzz";

  /** Field description */
  private static Logger logger = Logger.getLogger(WebUtil.class.getName());

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
   * @param response
   * @param resource
   */
  public static void addETagHeader(HttpServletResponse response,
                                   Resource resource)
  {
    response.addHeader(HEADER_ETAG, getETag(resource));
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param file
   */
  public static void addLastModifiedHeader(HttpServletResponse response,
          File file)
  {
    response.addDateHeader(HEADER_LASTMODIFIED, file.lastModified());
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param resource
   */
  public static void addLastModifiedHeader(HttpServletResponse response,
          Resource resource)
  {
    response.addHeader(HEADER_LASTMODIFIED,
                       formatHttpDate(resource.getLastModifiedDate()));
  }

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param response
   * @param seconds
   */
  public static void addStaticCacheControls(HttpServletRequest request,
          HttpServletResponse response, long seconds)
  {
    long time = new Date().getTime();

    response.addDateHeader(HEADER_EXPIRES, time + (seconds * 1000));

    StringBuffer cc = new StringBuffer("max-age=").append(seconds);

    cc.append(", ");

    if (SCHEME_HTTPS.equals(request.getScheme()))
    {
      cc.append("public");
    }
    else
    {
      cc.append("private");
    }

    response.addHeader(HEADER_CACHECONTROL, cc.toString());
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
    return getHttpDateFormat().format(date);
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
    return getHttpDateFormat().parse(dateString);
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
   * @param resource
   *
   * @return
   */
  public static String getETag(Resource resource)
  {
    return new StringBuffer("W/\"").append(resource.getSize()).append(
        resource.getLastModifiedDate().getTime()).append("\"").toString();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public static DateFormat getHttpDateFormat()
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat(HTTP_DATE_FORMAT,
                                    Locale.ENGLISH);

    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    return dateFormat;
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
      catch (NumberFormatException ex)
      {
        logger.warning(dateString);

        if (logger.isLoggable(Level.WARNING))
        {
          logger.log(Level.WARNING, dateString, ex);
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
    return isModified(request, file.lastModified(), getETag(file));
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param resource
   *
   * @return
   */
  public static boolean isModified(HttpServletRequest request,
                                   Resource resource)
  {
    return isModified(request, resource.getLastModifiedDate().getTime(),
                      getETag(resource));
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param lastModified
   * @param etag
   *
   * @return
   */
  public static boolean isModified(HttpServletRequest request,
                                   long lastModified, String etag)
  {
    boolean result = true;
    Date modifiedSince = getIfModifiedSinceDate(request);

    if (modifiedSince != null)
    {
      if (modifiedSince.getTime() == lastModified)
      {
        result = false;
      }
    }

    if (result)
    {
      String inmEtag = request.getHeader(HEADER_INM);

      if (Util.hasContent(inmEtag) && inmEtag.equals(etag))
      {
        result = false;
      }
    }

    return result;
  }
}
