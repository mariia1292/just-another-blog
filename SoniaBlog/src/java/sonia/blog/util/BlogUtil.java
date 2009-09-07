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



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.logging.LogManager;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogUtil
{

  /** Field description */
  private static Logger logger = Logger.getLogger(BlogUtil.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   */
  public static void configureLogger(BlogContext context)
  {
    if (context.isInstalled())
    {
      File logFile = new File(
                         context.getServletContext().getRealPath(
                           "/WEB-INF/config/logging.xml"));

      if (logFile.exists())
      {
        try
        {
          File logDir =
            context.getResourceManager().getDirectory(Constants.RESOURCE_LOG,
              true);
          LogManager logManager = LogManager.getInstance();

          logManager.putVar("logdir", logDir.getPath());
          logManager.readConfiguration(logFile);
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
      else
      {
        logger.severe("logging config not found");
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param year
   * @param month
   * @param day
   *
   * @return
   */
  public static Date createEndDate(Integer year, Integer month, Integer day)
  {
    if (year == null)
    {
      throw new IllegalArgumentException("year is null");
    }

    GregorianCalendar cal = new GregorianCalendar();

    cal.set(Calendar.YEAR, year);

    if (month != null)
    {
      cal.set(Calendar.MONTH, month);
    }
    else
    {
      cal.set(Calendar.MONTH, Calendar.DECEMBER);
    }

    if (day != null)
    {
      cal.set(Calendar.DAY_OF_MONTH, day);
    }
    else
    {
      cal.set(Calendar.DAY_OF_MONTH,
              cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);

    return cal.getTime();
  }

  /**
   * Method description
   *
   *
   * @param month
   * @param year
   *
   * @return
   */
  public static Date createEndDate(Integer year, Integer month)
  {
    return createEndDate(year, month, null);
  }

  /**
   * Method description
   *
   *
   * @param year
   *
   * @return
   */
  public static Date createEndDate(Integer year)
  {
    return createEndDate(year, null, null);
  }

  /**
   * Method description
   *
   *
   * @param month
   * @param year
   *
   * @return
   */
  public static Date createStartDate(Integer year, Integer month)
  {
    return createStartDate(year, month, null);
  }

  /**
   * Method description
   *
   *
   * @param year
   *
   * @return
   */
  public static Date createStartDate(Integer year)
  {
    return createStartDate(year, null, null);
  }

  /**
   * Method description
   *
   *
   * @param year
   * @param month
   * @param day
   *
   * @return
   */
  public static Date createStartDate(Integer year, Integer month, Integer day)
  {
    if (year == null)
    {
      throw new IllegalArgumentException("year is null");
    }

    GregorianCalendar cal = new GregorianCalendar();

    cal.set(Calendar.YEAR, year);

    if (month != null)
    {
      cal.set(Calendar.MONTH, month);
    }
    else
    {
      cal.set(Calendar.MONTH, Calendar.JANUARY);
    }

    if (day != null)
    {
      cal.set(Calendar.DAY_OF_MONTH, day);
    }
    else
    {
      cal.set(Calendar.DAY_OF_MONTH, 1);
    }

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 1);

    return cal.getTime();
  }

  /**
   * Method description
   *
   *
   * @param entry
   * @param url
   *
   * @return
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public static boolean sendTrackbackPing(Entry entry, URL url)
          throws IOException, SAXException, ParserConfigurationException
  {
    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer log = new StringBuffer();

      log.append("send ping to ").append(url);
      logger.info(log.toString());
    }

    Blog blog = entry.getBlog();
    URLConnection conn = url.openConnection();

    conn.setDoOutput(true);

    BufferedWriter writer = null;

    try
    {
      writer =
        new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

      String title = URLEncoder.encode(entry.getTitle(), "UTF-8");

      writer.write("title=");
      writer.write(title);

      StringBuffer link = new StringBuffer();

      link.append("/list/").append(entry.getId()).append(".jab");

      String urlString =
        BlogContext.getInstance().getLinkBuilder().buildLink(blog,
          link.toString());

      writer.write("&url=");
      writer.write(urlString);

      String content = getContent(entry);

      writer.write("&excerpt=");
      writer.write(content);

      String blogName = URLEncoder.encode(blog.getTitle(), "UTF-8");

      writer.write("&blog_name=");
      writer.write(blogName);
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }

    return checkResponse(conn);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public static List<Attachment> getAttachments(ContentObject object)
  {
    List<Attachment> attachments = null;

    if (object instanceof Entry)
    {
      attachments = ((Entry) object).getAttachments();
    }
    else
    {
      attachments = ((Page) object).getAttachments();
    }

    return attachments;
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public static BlogRequest getBlogRequest(Object object)
  {
    BlogRequest request = null;

    if (object instanceof BlogRequest)
    {
      request = (BlogRequest) object;
    }
    else if (object instanceof HttpServletRequest)
    {
      request = new BlogRequest((HttpServletRequest) object);
    }

    return request;
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public static BlogResponse getBlogResponse(Object object)
  {
    BlogResponse response = null;

    if (object instanceof BlogResponse)
    {
      response = (BlogResponse) object;
    }
    else if (object instanceof HttpServletResponse)
    {
      response = new BlogResponse((HttpServletResponse) object);
    }

    return response;
  }

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   *
   * @throws UnsupportedEncodingException
   */
  public static String getContent(Entry entry)
          throws UnsupportedEncodingException
  {
    String content = entry.getTeaser();

    if (Util.isBlank(content))
    {
      content = entry.getContent();
    }

    content = Util.extractHTMLText(content);

    if (content.length() > 255)
    {
      content = content.substring(0, 255);
    }

    content = URLEncoder.encode(content, "UTF-8");

    return content;
  }

  /**
   * Method description
   *
   *
   *
   * @param context
   * @return
   */
  public static SelectItem[] getLocaleItems(FacesContext context)
  {
    List<SelectItem> items = new ArrayList<SelectItem>();

    items.add(new SelectItem("---", "---"));

    Iterator<Locale> localeIterator =
      context.getApplication().getSupportedLocales();

    while (localeIterator.hasNext())
    {
      Locale locale = localeIterator.next();

      items.add(new SelectItem(locale, locale.getDisplayName(locale)));
    }

    return items.toArray(new SelectItem[0]);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param type
   * @param name
   * @param <T>
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T getRequestBean(BlogRequest request, Class<T> type,
                                     String name)
  {
    T result = null;
    Object obj = request.getAttribute(name);

    if (obj != null)
    {
      if (!type.isInstance(obj))
      {
        throw new BlogException("session object " + name
                                + " is not an instance of " + type.getName());
      }
      else
      {
        result = (T) obj;
      }
    }
    else
    {
      try
      {
        result = type.newInstance();

        if (result instanceof AbstractBean)
        {
          ((AbstractBean) result).init();
        }

        request.setAttribute(name, result);
      }
      catch (Exception ex)
      {
        throw new BlogException("could not create new RequestBean", ex);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param type
   * @param name
   * @param <T>
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T getSessionBean(BlogRequest request, Class<T> type,
                                     String name)
  {
    T result = null;
    HttpSession session = request.getSession(true);
    Object obj = session.getAttribute(name);

    if (obj != null)
    {
      if (!type.isInstance(obj))
      {
        throw new BlogException("session object " + name
                                + " is not an instance of " + type.getName());
      }
      else
      {
        result = (T) obj;
      }
    }
    else
    {
      try
      {
        result = type.newInstance();

        if (result instanceof AbstractBean)
        {
          ((AbstractBean) result).init();
        }

        session.setAttribute(name, result);
      }
      catch (Exception ex)
      {
        throw new BlogException("could not create new SessionBean", ex);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public static SelectItem[] getTimeZoneItems()
  {
    SelectItem[] items = null;
    String[] ids = TimeZone.getAvailableIDs();

    Arrays.sort(ids);

    int s = ids.length;

    items = new SelectItem[s];

    for (int i = 0; i < s; i++)
    {
      TimeZone timeZone = TimeZone.getTimeZone(ids[i]);

      items[i] = new SelectItem(timeZone, timeZone.getID());
    }

    return items;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param conn
   *
   * @return
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  private static boolean checkResponse(URLConnection conn)
          throws IOException, SAXException, ParserConfigurationException
  {
    boolean result = false;
    Document doc = XmlUtil.buildDocument(conn.getInputStream());
    NodeList list = doc.getElementsByTagName("error");

    if (XmlUtil.hasContent(list))
    {
      Node node = list.item(0);
      String errorCode = node.getTextContent();

      if (errorCode.trim().equals("0"))
      {
        result = true;
      }
      else
      {
        StringBuffer log = new StringBuffer();

        log.append("trackback ").append(conn.getURL());
        log.append(" returned errorcode ").append(errorCode);
        logger.warning(log.toString());
      }
    }

    return result;
  }
}
