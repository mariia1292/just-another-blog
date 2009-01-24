/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.calendar;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Blog;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseComponent;
import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
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

    BaseComponent calendar = (BaseComponent) component;

    if (isRendered(context, calendar))
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
      Date startDate = BlogUtil.createStartDate(month - 1, year);
      Date endDate = BlogUtil.createEndDate(month - 1, year);
      List<Date> dates = getEntryDates(blog, startDate, endDate);
      ResponseWriter writer = context.getResponseWriter();

      writer.startElement("table", component);

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
      writer.writeAttribute("href", buildLink(null, month, year, request),
                            null);
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
      writer.endElement("body");
      writer.endElement("table");
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
    String link = BlogContext.getInstance().getLinkBuilder().buildLink(request,
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
    return BlogContext.getDAOFactory().getEntryDAO().findAllCalendarDates(blog,
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
