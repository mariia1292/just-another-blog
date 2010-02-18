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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.logging.LogManager;

import sonia.macro.Macro;
import sonia.macro.MacroResult;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

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
   * @param request
   *
   * @return
   */
  public static boolean isFirstPage(BlogRequest request)
  {
    String uri = request.getRequestURI();
    String contextPath = request.getContextPath();

    return ((uri.length() == 0) || uri.equals("/")
            || uri.equals("/forward.jsp") || uri.equals(contextPath)
            || uri.equals(contextPath + "/")
            || uri.equals(contextPath + "/forward.jsp"));
  }


  /**
   * Method description
   *
   *
   * @param resources
   * @param result
   */
  public static void addWebMacroResources(List<WebResource> resources,
          MacroResult result)
  {
    List<Macro> macros = result.getMacros();

    if (Util.hasContent(macros))
    {
      for (Macro macro : macros)
      {
        if (macro instanceof WebMacro)
        {
          WebMacro wm = (WebMacro) macro;
          List<WebResource> wmRes = wm.getResources();

          if (wmRes != null)
          {
            resources.addAll(wmRes);
          }
        }
      }
    }
  }

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
}
