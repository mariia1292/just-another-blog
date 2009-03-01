/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.converter;

//~--- JDK imports ------------------------------------------------------------

import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author sdorra
 */
public class TimeZoneConverter implements Converter
{

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   * @param value
   *
   * @return
   */
  public Object getAsObject(FacesContext context, UIComponent component,
                            String value)
  {
    return TimeZone.getTimeZone(value);
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   * @param value
   *
   * @return
   */
  public String getAsString(FacesContext context, UIComponent component,
                            Object value)
  {
    String id = null;

    if ((value != null) && (value instanceof TimeZone))
    {
      id = ((TimeZone) value).getID();
    }

    return id;
  }
}
