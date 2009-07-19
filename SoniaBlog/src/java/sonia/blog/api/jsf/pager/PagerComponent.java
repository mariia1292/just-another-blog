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



package sonia.blog.api.jsf.pager;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class PagerComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.pager";

  /** Field description */
  public static final String RENDERER = "sonia.blog.pager.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public PagerComponent()
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
    state = (Object[]) obj;
    super.restoreState(context, state[0]);
    forParam = (String) state[1];
    showPages = (Integer) state[2];
    selectedStyle = (String) state[3];
    selectedStyleClass = (String) state[4];
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
    if (state == null)
    {
      state = new Object[5];
    }

    state[0] = super.saveState(context);
    state[1] = forParam;
    state[2] = showPages;
    state[3] = selectedStyle;
    state[4] = selectedStyleClass;

    return state;
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

  /**
   * Method description
   *
   *
   * @return
   */
  public String getFor()
  {
    if (forParam != null)
    {
      return forParam;
    }

    ValueExpression ve = getValueExpression("for");

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
  public String getSelectedStyle()
  {
    if (selectedStyle != null)
    {
      return selectedStyle;
    }

    ValueExpression ve = getValueExpression("selectedStyle");

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
  public String getSelectedStyleClass()
  {
    if (selectedStyleClass != null)
    {
      return selectedStyleClass;
    }

    ValueExpression ve = getValueExpression("selectedStyleClass");

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
  public Integer getShowPages()
  {
    if (showPages != null)
    {
      return showPages;
    }

    ValueExpression ve = getValueExpression("showPages");

    return (ve != null)
           ? (Integer) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param forParam
   */
  public void setFor(String forParam)
  {
    this.forParam = forParam;
  }

  /**
   * Method description
   *
   *
   * @param selectedStyle
   */
  public void setSelectedStyle(String selectedStyle)
  {
    this.selectedStyle = selectedStyle;
  }

  /**
   * Method description
   *
   *
   * @param selectedStyleClass
   */
  public void setSelectedStyleClass(String selectedStyleClass)
  {
    this.selectedStyleClass = selectedStyleClass;
  }

  /**
   * Method description
   *
   *
   * @param showPages
   */
  public void setShowPages(Integer showPages)
  {
    this.showPages = showPages;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String forParam;

  /** Field description */
  private String selectedStyle;

  /** Field description */
  private String selectedStyleClass;

  /** Field description */
  private Integer showPages;

  /** Field description */
  private Object[] state;
}
