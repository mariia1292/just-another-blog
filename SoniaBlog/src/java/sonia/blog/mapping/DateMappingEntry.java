/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

import javax.faces.model.ListDataModel;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class DateMappingEntry extends ScrollableMappingEntry
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   * @param start
   * @param end
   *
   * @return
   */
  @Override
  public boolean handleMapping(BlogRequest request, BlogResponse response,
                               String[] param, int start, int end)
  {
    String viewId = VIEW_NOTFOUND;

    if ((param != null) && (param.length > 0))
    {
      String[] dateString = param[0].split("-");

      try
      {
        Integer year = Integer.parseInt(dateString[0]);
        Integer month = null;
        Integer day = null;
        Long id = null;

        if (dateString.length > 1)
        {
          month = Integer.parseInt(dateString[1]);

          if (dateString.length > 2)
          {
            day = Integer.parseInt(dateString[2]);
          }
        }

        if ((param.length > 1) && (param[1].length() > 4))
        {
          String idString = param[1].substring(0, param[1].length() - 4);

          if (!idString.equalsIgnoreCase("index"))
          {
            id = Long.parseLong(idString);
          }
        }

        viewId = handleMapping(request, id, year, month, day, start, end);
      }
      catch (NumberFormatException ex)
      {
        logger.log(Level.FINE, null, ex);
      }
    }

    setViewId(request, viewId);

    return true;
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
  private Date buildEndDate(Integer year, Integer month, Integer day)
  {
    GregorianCalendar calendar = new GregorianCalendar();

    if ((month == null) || (month <= 0))
    {
      month = 12;
    }

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);

    if ((day == null) || (day <= 0))
    {
      day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);

    return calendar.getTime();
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private List<Entry> buildList(Blog blog, Date startDate, Date endDate)
  {
    List<Entry> entries = null;
    EntityManager em = BlogContext.getInstance().getEntityManager();

    try
    {
      Query q = em.createNamedQuery("Entry.findByDate");

      q.setParameter("blog", blog);
      q.setParameter("start", startDate);
      q.setParameter("end", endDate);
      entries = q.getResultList();
    }
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return entries;
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
  private Date buildStartDate(Integer year, Integer month, Integer day)
  {
    Calendar calendar = new GregorianCalendar();

    if ((month == null) || (month <= 0))
    {
      month = 1;
    }

    if ((day == null) || (day <= 0))
    {
      day = 1;
    }

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 1);

    return calendar.getTime();
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param id
   * @param year
   * @param month
   * @param day
   * @param start
   * @param end
   *
   * @return
   */
  private String handleMapping(BlogRequest request, Long id, Integer year,
                               Integer month, Integer day, int start, int end)
  {
    String result = VIEW_NOTFOUND;
    Blog blog = request.getCurrentBlog();
    Date startDate = buildStartDate(year, month, day);
    Date endDate = buildEndDate(year, month, day);
    List<Entry> entries = buildList(blog, startDate, endDate);

    if (entries != null)
    {
      BlogBean blogBean = getBlogBean(request);

      end = (end < entries.size())
            ? end
            : entries.size();
      Collections.reverse(entries);

      List<Entry> pageEntries = entries.subList(start, end);

      blogBean.setEntries(entries);
      blogBean.setPageEntries(new ListDataModel(pageEntries));

      if (id != null)
      {
        Entry entry = null;

        for (Entry e : entries)
        {
          if (e.getId().equals(id))
          {
            entry = e;

            break;
          }
        }

        if (entry != null)
        {
          blogBean.setEntry(entry);
          result = VIEW_DETAIL;
        }
      }
      else
      {
        result = VIEW_LIST;
      }
    }

    return result;
  }
}
