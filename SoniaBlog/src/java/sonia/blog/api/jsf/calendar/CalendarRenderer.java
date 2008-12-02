/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.calendar;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseComponent;
import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public class CalendarRenderer extends BaseRenderer
{
  private static final String STYLE_TODAY = "today";

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

      int currentDay = cal.get( Calendar.DAY_OF_MONTH ) - 1;
      cal.set(Calendar.DAY_OF_MONTH, 1);

      int month = cal.get(Calendar.MONTH) + 1;
      int year = cal.get(Calendar.YEAR);
      int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
      int firstDay = cal.get(Calendar.DAY_OF_WEEK);
      int daysOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
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
      writer.write(month + " " + year);
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
          if ( counter == currentDay )
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
              printDay(writer, counter, style);
            }
          }
          else if ((counter > 0) && (counter < daysOfMonth))
          {
            counter++;
            printDay(writer, counter, style);
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
   * @param writer
   * @param day
   * @param styleClass
   *
   * @throws IOException
   */
  private void printDay(ResponseWriter writer, int day, String styleClass)
          throws IOException
  {
    writer.startElement("span", null);
    writer.writeAttribute("class", styleClass, null);
    writer.writeText(day, null);
    writer.endElement("span");
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
