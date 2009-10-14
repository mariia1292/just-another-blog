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



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.util.BlogUtil;

import sonia.cache.Cacheable;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

/**
 *
 * @author Sebastian Sdorra
 */
@Cacheable
public class CalendarMapping extends FinalMapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(CalendarMapping.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    Calendar calendar = getCalendar(request);
    PrintWriter writer = null;
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    Date startDate = BlogUtil.createStartDate(year, month);
    Date endData = BlogUtil.createEndDate(year, month);
    List<Date> events = entryDAO.findAllCalendarDates(request.getCurrentBlog(),
                          startDate, endData);
    Calendar today = Calendar.getInstance();

    try
    {
      writer = response.getWriter();
      writer.print("{\"year\":");
      writer.print(year);
      writer.print(", \"month\":");
      writer.print(month + 1);
      writer.print(", \"weeks\":");
      writer.print(calendar.getActualMaximum(Calendar.WEEK_OF_MONTH));
      writer.print(", \"firstDay\":");
      writer.print(calendar.get(Calendar.DAY_OF_WEEK));
      writer.print(", \"daysOfMonth\":");
      writer.print(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

      if (today.get(Calendar.MONTH) == month)
      {
        writer.print(", \"today\":");
        writer.print(today.get(Calendar.DAY_OF_MONTH));
      }

      writer.print(", \"events\": \"");

      if (Util.hasContent(events))
      {
        List<Integer> days = getDays(events);
        Iterator<Integer> dayIt = days.iterator();

        while (dayIt.hasNext())
        {
          Integer day = dayIt.next();

          writer.print(day);

          if (dayIt.hasNext())
          {
            writer.print(",");
          }
        }
      }

      writer.println("\"}");
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private Calendar getCalendar(BlogRequest request)
  {
    Calendar calendar = null;
    int year = -1;
    int month = -1;

    try
    {
      String yearParam = request.getParameter("year");
      String monthParam = request.getParameter("month");

      if (Util.hasContent(yearParam))
      {
        year = Integer.parseInt(yearParam);
      }
      else
      {
        year = Calendar.getInstance().get(Calendar.YEAR);
      }

      if (Util.hasContent(monthParam))
      {
        month = Integer.parseInt(monthParam) - 1;
      }
      else
      {
        month = Calendar.getInstance().get(Calendar.MONTH);
      }

      calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.DAY_OF_MONTH, 1);
    }
    catch (NumberFormatException ex)
    {
      logger.log(Level.WARNING, null, ex);
    }

    return calendar;
  }

  /**
   * Method description
   *
   *
   * @param dates
   *
   * @return
   */
  private List<Integer> getDays(List<Date> dates)
  {
    List<Integer> days = new ArrayList<Integer>();
    Calendar cal = null;

    for (Date date : dates)
    {
      cal = Calendar.getInstance();
      cal.setTime(date);

      Integer day = Integer.valueOf(cal.get(Calendar.DAY_OF_MONTH));

      if (!days.contains(day))
      {
        days.add(day);
      }
    }

    return days;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private EntryDAO entryDAO;
}
