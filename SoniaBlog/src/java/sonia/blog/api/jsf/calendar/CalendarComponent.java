/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.calendar;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseComponent;

/**
 *
 * @author sdorra
 */
public class CalendarComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.calendar";

  /** Field description */
  public static final String RENDERER = "sonia.blog.calendar.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public CalendarComponent()
  {
    setRendererType(RENDERER);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getFamily()
  {
    return FAMILY;
  }
}
