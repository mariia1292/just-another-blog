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

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author Sebastian Sdorra
 */
public class CalendarTag extends BaseTag
{

  /**
   * Method description
   *
   */
  @Override
  public void release()
  {
    super.release();
    enableAjax = null;
    ajaxUrl = null;
    dayUrlPattern = null;
    monthUrlPattern = null;
    yearUrlPattern = null;
    loadImage = null;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getComponentType()
  {
    return CalendarComponent.FAMILY;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getRendererType()
  {
    return CalendarComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param ajaxUrl
   */
  public void setAjaxUrl(ValueExpression ajaxUrl)
  {
    this.ajaxUrl = ajaxUrl;
  }

  /**
   * Method description
   *
   *
   * @param dayUrlPattern
   */
  public void setDayUrlPattern(ValueExpression dayUrlPattern)
  {
    this.dayUrlPattern = dayUrlPattern;
  }

  /**
   * Method description
   *
   *
   * @param enableAjax
   */
  public void setEnableAjax(ValueExpression enableAjax)
  {
    this.enableAjax = enableAjax;
  }

  /**
   * Method description
   *
   *
   * @param loadImage
   */
  public void setLoadImage(ValueExpression loadImage)
  {
    this.loadImage = loadImage;
  }

  /**
   * Method description
   *
   *
   * @param monthUrlPattern
   */
  public void setMonthUrlPattern(ValueExpression monthUrlPattern)
  {
    this.monthUrlPattern = monthUrlPattern;
  }

  /**
   * Method description
   *
   *
   * @param yearUrlPattern
   */
  public void setYearUrlPattern(ValueExpression yearUrlPattern)
  {
    this.yearUrlPattern = yearUrlPattern;
  }

  /**
   * Method description
   *
   *
   * @param component
   */
  @Override
  protected void setProperties(UIComponent component)
  {
    super.setProperties(component);

    if (enableAjax != null)
    {
      component.setValueExpression("enableAjax", enableAjax);
    }

    if (ajaxUrl != null)
    {
      component.setValueExpression("ajaxUrl", ajaxUrl);
    }

    if (dayUrlPattern != null)
    {
      component.setValueExpression("dayUrlPattern", dayUrlPattern);
    }

    if (monthUrlPattern != null)
    {
      component.setValueExpression("monthUrlPattern", monthUrlPattern);
    }

    if (yearUrlPattern != null)
    {
      component.setValueExpression("yearUrlPattern", yearUrlPattern);
    }

    if (loadImage != null)
    {
      component.setValueExpression("loadImage", loadImage);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression ajaxUrl;

  /** Field description */
  private ValueExpression dayUrlPattern;

  /** Field description */
  private ValueExpression enableAjax;

  /** Field description */
  private ValueExpression loadImage;

  /** Field description */
  private ValueExpression monthUrlPattern;

  /** Field description */
  private ValueExpression yearUrlPattern;
}
