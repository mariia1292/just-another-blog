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



package sonia.blog.api.jsf.search;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author Sebastian Sdorra
 */
public class SearchTag extends BaseTag
{

  /**
   * Method description
   *
   */
  @Override
  public void release()
  {
    title = null;
    target = null;
    type = null;
    value = null;
    label = null;
    super.release();
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
    return SearchComponent.FAMILY;
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
    return SearchComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param autoComplete
   */
  public void setAutoComplete(ValueExpression autoComplete)
  {
    this.autoComplete = autoComplete;
  }

  /**
   * Method description
   *
   *
   * @param label
   */
  public void setLabel(ValueExpression label)
  {
    this.label = label;
  }

  /**
   * Method description
   *
   *
   * @param target
   */
  public void setTarget(ValueExpression target)
  {
    this.target = target;
  }

  /**
   * Method description
   *
   *
   * @param title
   */
  public void setTitle(ValueExpression title)
  {
    this.title = title;
  }

  /**
   * Method description
   *
   *
   * @param type
   */
  public void setType(ValueExpression type)
  {
    this.type = type;
  }

  /**
   * Method description
   *
   *
   * @param value
   */
  public void setValue(ValueExpression value)
  {
    this.value = value;
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
    if (title != null)
    {
      component.setValueExpression("title", title);
    }

    if (target != null)
    {
      component.setValueExpression("target", target);
    }

    if (value != null)
    {
      component.setValueExpression("value", value);
    }

    if (type != null)
    {
      component.setValueExpression("type", type);
    }

    if (label != null)
    {
      component.setValueExpression("label", label);
    }

    if (autoComplete != null)
    {
      component.setValueExpression("autoComplete", autoComplete);
    }

    super.setProperties(component);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression autoComplete;

  /** Field description */
  private ValueExpression label;

  /** Field description */
  private ValueExpression target;

  /** Field description */
  private ValueExpression title;

  /** Field description */
  private ValueExpression type;

  /** Field description */
  private ValueExpression value;
}
