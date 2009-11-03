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



package sonia.jsf.base;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class BaseComponent extends UIComponentBase
{

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
    style = (String) state[1];
    styleClass = (String) state[2];
    role = (String) state[3];
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
    Object[] state = new Object[4];

    state[0] = super.saveState(context);
    state[1] = style;
    state[2] = styleClass;
    state[3] = role;

    return state;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRole()
  {
    if (role != null)
    {
      return role;
    }

    ValueExpression ve = getValueExpression("role");

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
  public String getStyle()
  {
    if (style != null)
    {
      return style;
    }

    ValueExpression ve = getValueExpression("style");

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
  public String getStyleClass()
  {
    if (styleClass != null)
    {
      return styleClass;
    }

    ValueExpression ve = getValueExpression("styleClass");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param role
   */
  public void setRole(String role)
  {
    this.role = role;
  }

  /**
   * Method description
   *
   *
   * @param style
   */
  public void setStyle(String style)
  {
    this.style = style;
  }

  /**
   * Method description
   *
   *
   * @param styleClass
   */
  public void setStyleClass(String styleClass)
  {
    this.styleClass = styleClass;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String role;

  /** Field description */
  private String style;

  /** Field description */
  private String styleClass;
}
