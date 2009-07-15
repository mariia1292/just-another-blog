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

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author Sebastian Sdorra
 */
public class PagerTag extends BaseTag
{

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getComponentType()
  {
    return PagerComponent.FAMILY;
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
    return PagerComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param forParam
   */
  public void setFor(ValueExpression forParam)
  {
    this.forParam = forParam;
  }

  /**
   * Method description
   *
   *
   * @param selectedStyle
   */
  public void setSelectedStyle(ValueExpression selectedStyle)
  {
    this.selectedStyle = selectedStyle;
  }

  /**
   * Method description
   *
   *
   * @param selectedStyleClass
   */
  public void setSelectedStyleClass(ValueExpression selectedStyleClass)
  {
    this.selectedStyleClass = selectedStyleClass;
  }

  /**
   * Method description
   *
   *
   * @param showPages
   */
  public void setShowPages(ValueExpression showPages)
  {
    this.showPages = showPages;
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

    if (forParam != null)
    {
      component.setValueExpression("for", forParam);
    }

    if (showPages != null)
    {
      component.setValueExpression("showPages", showPages);
    }

    if (selectedStyle != null)
    {
      component.setValueExpression("selectedStyle", selectedStyle);
    }

    if (selectedStyleClass != null)
    {
      component.setValueExpression("selectedStyleClass", selectedStyleClass);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression forParam;

  /** Field description */
  private ValueExpression selectedStyle;

  /** Field description */
  private ValueExpression selectedStyleClass;

  /** Field description */
  private ValueExpression showPages;
}