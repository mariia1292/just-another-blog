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



package sonia.blog.api.jsf.calendar;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Blog;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseComponent;
import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Sebastian Sdorra
 */
public class CalendarRenderer extends BaseRenderer
{

  /** Field description */
  private static final String STYLE_TODAY = "today";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   *
   * @throws IOException
   */
  @Override
  public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException
  {
    if (!(component instanceof BaseComponent))
    {
      throw new IllegalArgumentException("wrong component");
    }

    CalendarComponent calendar = (CalendarComponent) component;

    if (isRendered(context, calendar))
    {
      if (calendar.isEnableAjax())
      {
        encodeAjaxCalendar(context, calendar);
      }
      else
      {
        encodeHtmlCalendar(context, calendar);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param options
   * @param lb
   * @param request
   * @param name
   * @param value
   */
  private void appendUrlOption(List<String> options, LinkBuilder lb,
                               BlogRequest request, String name, String value)
  {
    if (value != null)
    {
      StringBuffer option = new StringBuffer();

      option.append("\"").append(name).append("\": \"");
      option.append(lb.getRelativeLink(request, value)).append("\"");
      options.add(option.toString());
    }
  }

  /**
   * Method description
   *
   *
   * @param day
   * @param month
   * @param year
   * @param request
   *
   * @return
   */
  private String buildLink(Integer day, Integer month, Integer year,
                           BlogRequest request)
  {
    String link =
      BlogContext.getInstance().getLinkBuilder().getRelativeLink(request,
        "/date/");

    link += year;

    if (month != null)
    {
      link += "-" + month;

      if (day != null)
      {
        link += "-" + day;
      }
    }

    return link + "/index.jab";
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param calendar
   *
   * @throws IOException
   */
  private void encodeAjaxCalendar(FacesContext context,
                                  CalendarComponent calendar)
          throws IOException
  {
    ResponseWriter writer = context.getResponseWriter();

    writer.startElement("div", calendar);
    writer.writeAttribute("class", "calendar", null);
    writer.endElement("div");
    writer.startElement("script", calendar);
    writer.writeAttribute("type", "text/javascript", null);

    if (context.getExternalContext().getRequestMap().get(
            "sonia.blog.resource.calendar") == null)
    {
      writer.write("$.getScript(\"");
      writer.write(context.getExternalContext().getRequestContextPath());
      writer.write("/resources/jquery/plugins/js/jquery.calendar.js");
      writer.write("\", function(){ ");
      encodeAjaxCalendarScript(context, calendar, writer);
      writer.write("}); ");
      context.getExternalContext().getRequestMap().put(
          "sonia.blog.resource.calendar", Boolean.TRUE);
    }
    else
    {
      writer.write("$.ready(function(){");
      encodeAjaxCalendarScript(context, calendar, writer);
      writer.write("});");
    }

    writer.endElement("script");
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param calendar
   * @param writer
   *
   * @throws IOException
   */
  private void encodeAjaxCalendarScript(FacesContext context,
          CalendarComponent calendar, ResponseWriter writer)
          throws IOException
  {
    BlogRequest request =
      BlogUtil.getBlogRequest(context.getExternalContext().getRequest());
    LinkBuilder lb = BlogContext.getInstance().getLinkBuilder();

    writer.write("$(\"div.calendar\").calendar(\"");
    writer.write(lb.getRelativeLink(request, calendar.getAjaxUrl()));
    writer.write("\", {\n");

    List<String> options = new ArrayList<String>();

    appendUrlOption(options, lb, request, "dayUrlPattern",
                    calendar.getDayUrlPattern());
    appendUrlOption(options, lb, request, "monthUrlPattern",
                    calendar.getMonthUrlPattern());
    appendUrlOption(options, lb, request, "yearUrlPattern",
                    calendar.getYearUrlPattern());
    appendUrlOption(options, lb, request, "loadingImage",
                    calendar.getLoadImage());

    Iterator<String> optionIt = options.iterator();

    while (optionIt.hasNext())
    {
      String option = optionIt.next();

      writer.write(option);

      if (optionIt.hasNext())
      {
        writer.write(",\n");
      }
    }

    writer.write("});\n");
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param calendar
   *
   * @throws IOException
   */
  private void encodeHtmlCalendar(FacesContext context,
                                  CalendarComponent calendar)
          throws IOException
  {
    GregorianCalendar cal = new GregorianCalendar();
    int currentDay = cal.get(Calendar.DAY_OF_MONTH) - 1;

    cal.set(Calendar.DAY_OF_MONTH, 1);

    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);
    int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
    int firstDay = cal.get(Calendar.DAY_OF_WEEK);
    int daysOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    BlogRequest request =
      (BlogRequest) context.getExternalContext().getRequest();
    Blog blog = request.getCurrentBlog();
    Date startDate = BlogUtil.createStartDate(year, month - 1);
    Date endDate = BlogUtil.createEndDate(year, month - 1);
    List<Date> dates = getEntryDates(blog, startDate, endDate);
    ResponseWriter writer = context.getResponseWriter();

    writer.startElement("table", calendar);

    if (!isBlank(calendar.getStyleClass()))
    {
      writer.writeAttribute("class", calendar.getStyleClass(), null);
    }
    else
    {
      writer.writeAttribute("class", "jabCalendar", null);
    }

    if (!isBlank(calendar.getStyle()))
    {
      writer.writeAttribute("style", calendar.getStyle(), null);
    }

    writer.startElement("thead", null);
    writer.startElement("tr", null);
    writer.startElement("th", null);
    writer.writeAttribute("colspan", "7", null);
    writer.startElement("a", null);
    writer.writeAttribute("href", buildLink(null, month, year, request), null);
    writer.write("" + month);
    writer.endElement("a");
    writer.write(" ");
    writer.startElement("a", null);
    writer.writeAttribute("href", buildLink(null, null, year, request), null);
    writer.write("" + year);
    writer.endElement("a");
    writer.endElement("th");
    writer.endElement("tr");
    writer.endElement("thead");
    writer.startElement("tbody", null);

    // start WeedHeader
    writer.startElement("tr", null);
    printDayHeader(writer, "Mo", STYLE_WEEKDAYHEADER);
    printDayHeader(writer, "Di", STYLE_WEEKDAYHEADER);
    printDayHeader(writer, "Mi", STYLE_WEEKDAYHEADER);
    printDayHeader(writer, "Do", STYLE_WEEKDAYHEADER);
    printDayHeader(writer, "Fr", STYLE_WEEKDAYHEADER);
    printDayHeader(writer, "Sa", STYLE_WEEKENDDAYHEADER);
    printDayHeader(writer, "So", STYLE_WEEKENDDAYHEADER);
    writer.endElement("tr");

    // end WeedHeader
    // start month
    int counter = 0;

    for (int i = 0; i < weeks; i++)
    {
      writer.startElement("tr", null);

      for (int j = 0; j < 7; j++)
      {
        writer.startElement("td", null);

        if (counter == currentDay)
        {
          writer.writeAttribute("class", STYLE_TODAY, null);
        }

        String style = (j < 5)
                       ? STYLE_WEEKDAY
                       : STYLE_WEEKENDDAY;

        if (i == 0)
        {
          if (j >= firstDay - 2)
          {
            counter++;
            printDay(writer, request, counter, month, year, style, dates);
          }
        }
        else if ((counter > 0) && (counter < daysOfMonth))
        {
          counter++;
          printDay(writer, request, counter, month, year, style, dates);
        }

        writer.endElement("td");
      }

      writer.endElement("tr");
    }

    // end month
    writer.endElement("tbody");
    writer.endElement("table");
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param request
   * @param day
   * @param month
   * @param year
   * @param styleClass
   * @param dates
   *
   * @throws IOException
   */
  private void printDay(ResponseWriter writer, BlogRequest request, int day,
                        int month, int year, String styleClass,
                        List<Date> dates)
          throws IOException
  {
    boolean event = hasEvent(day, dates);

    if (event)
    {
      writer.startElement("a", null);

      String link = buildLink(day, month, year, request);

      writer.writeAttribute("href", link, null);
    }
    else
    {
      writer.startElement("span", null);
    }

    writer.writeAttribute("class", styleClass, null);
    writer.writeText(day, null);
    writer.endElement(event
                      ? "a"
                      : "span");
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param day
   * @param styleClass
   *
   * @throws IOException
   */
  private void printDayHeader(ResponseWriter writer, String day,
                              String styleClass)
          throws IOException
  {
    writer.startElement("td", null);
    writer.startElement("span", null);
    writer.writeAttribute("class", styleClass, null);
    writer.writeText(day, null);
    writer.endElement("span");
    writer.endElement("td");
  }

  //~--- get methods ----------------------------------------------------------

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
  private List<Date> getEntryDates(Blog blog, Date startDate, Date endDate)
  {
    return BlogContext.getDAOFactory().getEntryDAO().getAllCalendarDates(blog,
            startDate, endDate);
  }

  /**
   * Method description
   *
   *
   * @param day
   * @param dates
   *
   * @return
   */
  private boolean hasEvent(int day, List<Date> dates)
  {
    boolean result = false;

    for (Date date : dates)
    {
      GregorianCalendar cal = new GregorianCalendar();

      cal.setTime(date);

      if (cal.get(Calendar.DAY_OF_MONTH) == day)
      {
        result = true;

        break;
      }
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String STYLE_WEEKDAY = "weekday";

  /** Field description */
  private String STYLE_WEEKDAYHEADER = "weekdayHeader";

  /** Field description */
  private String STYLE_WEEKENDDAY = "weekendday";

  /** Field description */
  private String STYLE_WEEKENDDAYHEADER = "weekenddayHeader";
}
