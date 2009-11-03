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

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param obj
   */
  @Override
  public void restoreState(FacesContext context, Object obj)
  {
    Object[] state = (Object[]) obj;

    super.restoreState(context, state[0]);
    enableAjax = (Boolean) state[1];
    ajaxUrl = (String) state[2];
    dayUrlPattern = (String) state[3];
    monthUrlPattern = (String) state[4];
    yearUrlPattern = (String) state[5];
    loadImage = (String) state[6];
  }

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  @Override
  public Object saveState(FacesContext context)
  {
    Object[] state = new Object[7];

    state[0] = super.saveState(context);
    state[1] = enableAjax;
    state[2] = ajaxUrl;
    state[3] = dayUrlPattern;
    state[4] = monthUrlPattern;
    state[5] = yearUrlPattern;
    state[6] = loadImage;

    return state;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAjaxUrl()
  {
    if (ajaxUrl != null)
    {
      return ajaxUrl;
    }

    ValueExpression ve = getValueExpression("ajaxUrl");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDayUrlPattern()
  {
    if (dayUrlPattern != null)
    {
      return dayUrlPattern;
    }

    ValueExpression ve = getValueExpression("dayUrlPattern");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

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

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLoadImage()
  {
    if (loadImage != null)
    {
      return loadImage;
    }

    ValueExpression ve = getValueExpression("loadImage");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMonthUrlPattern()
  {
    if (monthUrlPattern != null)
    {
      return monthUrlPattern;
    }

    ValueExpression ve = getValueExpression("monthUrlPattern");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getYearUrlPattern()
  {
    if (yearUrlPattern != null)
    {
      return yearUrlPattern;
    }

    ValueExpression ve = getValueExpression("yearUrlPattern");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEnableAjax()
  {
    if (enableAjax != null)
    {
      return enableAjax;
    }

    ValueExpression ve = getValueExpression("enableAjax");

    return (ve != null)
           ? (Boolean) ve.getValue(getFacesContext().getELContext())
           : false;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param ajaxUrl
   */
  public void setAjaxUrl(String ajaxUrl)
  {
    this.ajaxUrl = ajaxUrl;
  }

  /**
   * Method description
   *
   *
   * @param dayUrlPattern
   */
  public void setDayUrlPattern(String dayUrlPattern)
  {
    this.dayUrlPattern = dayUrlPattern;
  }

  /**
   * Method description
   *
   *
   * @param enableAjax
   */
  public void setEnableAjax(boolean enableAjax)
  {
    this.enableAjax = enableAjax;
  }

  /**
   * Method description
   *
   *
   * @param loadImage
   */
  public void setLoadImage(String loadImage)
  {
    this.loadImage = loadImage;
  }

  /**
   * Method description
   *
   *
   * @param monthUrlPattern
   */
  public void setMonthUrlPattern(String monthUrlPattern)
  {
    this.monthUrlPattern = monthUrlPattern;
  }

  /**
   * Method description
   *
   *
   * @param yearUrlPattern
   */
  public void setYearUrlPattern(String yearUrlPattern)
  {
    this.yearUrlPattern = yearUrlPattern;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String ajaxUrl;

  /** Field description */
  private String dayUrlPattern;

  /** Field description */
  private Boolean enableAjax;

  /** Field description */
  private String loadImage;

  /** Field description */
  private String monthUrlPattern;

  /** Field description */
  private String yearUrlPattern;
}
