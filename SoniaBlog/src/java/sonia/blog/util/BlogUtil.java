/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogRuntimeException;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author sdorra
 */
public class BlogUtil
{

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
        throw new BlogRuntimeException("session object " + name
                                       + " is not an instance of "
                                       + type.getName());
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
        throw new BlogRuntimeException("could not create new SessionBean", ex);
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
