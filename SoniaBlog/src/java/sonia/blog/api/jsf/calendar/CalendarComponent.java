/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.api.jsf.calendar;

import sonia.jsf.base.BaseComponent;

/**
 *
 * @author sdorra
 */
public class CalendarComponent extends BaseComponent {

    /** Field description */
  public static final String FAMILY = "sonia.blog.calendar";

  /** Field description */
  public static final String RENDERER = "sonia.blog.calendar.renderer";

  public CalendarComponent()
  {
    setRendererType(RENDERER);
  }



  @Override
  public String getFamily()
  {
    return FAMILY;
  }

}
