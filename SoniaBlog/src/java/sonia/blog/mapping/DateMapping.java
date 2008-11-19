/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class DateMapping extends AbstractMappingHandler
{

  /**
   * Method description
   *
   *
   * @param context
   * @param blog
   * @param args
   *
   * @return
   */
  public String handleMapping(FacesContext context, Blog blog, String[] args)
  {
    String result = "list.xhtml";

    if ((args != null) && (args.length > 0))
    {
      int position = 0;
      int max = blog.getEntriesPerPage();

      if (args.length > 1)
      {
        try
        {
          position = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException ex)
        {
          logger.log(Level.FINEST, null, ex);
        }
      }

      String[] dateString = args[0].split("-");

      if (dateString.length > 0)
      {
        try
        {
          int year = Integer.parseInt(dateString[0]);
          Calendar startCal = createStartCalendar(year);
          Calendar endCal = createEndCalendar(year);

          if (dateString.length > 2)
          {
            int day = Integer.parseInt(dateString[2]);
            int month = Integer.parseInt(dateString[1]) - 1;

            startCal.set(Calendar.MONTH, month);
            startCal.set(Calendar.DAY_OF_MONTH, day);
            endCal.set(Calendar.MONTH, month);
            endCal.set(Calendar.DAY_OF_MONTH, day);
          }
          else if (dateString.length > 1)
          {
            int month = Integer.parseInt(dateString[1]) - 1;

            startCal.set(Calendar.MONTH, month);
            startCal.set(Calendar.DAY_OF_MONTH, 1);
            endCal.set(Calendar.MONTH, month);
            endCal.set(Calendar.DAY_OF_MONTH,
                       endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
          }
          else
          {
            startCal.set(Calendar.MONTH, Calendar.JANUARY);
            startCal.set(Calendar.DAY_OF_MONTH, 1);
            endCal.set(Calendar.MONTH, Calendar.DECEMBER);
            endCal.set(Calendar.DAY_OF_MONTH,
                       endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
          }

          EntityManager em = BlogContext.getInstance().getEntityManager();
          Query q = em.createNamedQuery("Entry.findByDate");

          q.setParameter("blog", blog);
          q.setParameter("start", startCal.getTime());
          q.setParameter("end", endCal.getTime());

          List list = q.getResultList();
          String prefix = BlogContext.getInstance().getLinkBuilder().buildLink(
                              getRequest(context),
                              "/blog/date/" + args[0] + "/");

          setEntries(getBlogBean(context), list, position, max, prefix);
          em.close();
        }
        catch (NumberFormatException ex)
        {
          logger.log(Level.FINEST, null, ex);
        }
      }
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMappingName()
  {
    return "date";
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param year
   *
   * @return
   */
  private Calendar createEndCalendar(int year)
  {
    GregorianCalendar cal = new GregorianCalendar();

    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.YEAR, year);

    return cal;
  }

  /**
   * Method description
   *
   *
   * @param year
   *
   * @return
   */
  private Calendar createStartCalendar(int year)
  {
    GregorianCalendar cal = new GregorianCalendar();

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.YEAR, year);

    return cal;
  }
}
