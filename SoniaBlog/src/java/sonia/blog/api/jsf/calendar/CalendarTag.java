/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.api.jsf.calendar;

import sonia.jsf.base.BaseTag;

/**
 *
 * @author sdorra
 */
public class CalendarTag extends BaseTag{

  @Override
  public String getComponentType()
  {
    return CalendarComponent.FAMILY;
  }

  @Override
  public String getRendererType()
  {
    return CalendarComponent.RENDERER;
  }

}
