/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.converter;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author sdorra
 */
public class LocaleConverter implements Converter
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
    Locale locale = null;

    if (!Util.isBlank(value) &&!value.endsWith("---"))
    {
      locale = new Locale(value);
    }

    return locale;
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

    if ((value != null) && (value instanceof Locale))
    {
      id = ((Locale) value).getLanguage();
    }

    return id;
  }
}
