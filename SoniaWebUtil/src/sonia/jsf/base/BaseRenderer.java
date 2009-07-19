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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 *
 * @author Sebastian Sdorra
 */
public class BaseRenderer extends Renderer
{

  /**
   * Method description
   *
   *
   * @param context
   * @param link
   *
   * @return
   */
  protected String buildRelativeLink(FacesContext context, String link)
  {
    if (link.startsWith("/"))
    {
      String contextPath = context.getExternalContext().getRequestContextPath();

      link = contextPath + link;
    }

    return link;
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param value
   *
   * @return
   */
  protected UIOutput createHtmlOutputComponent(FacesContext context,
          String value)
  {
    Boolean escape = Boolean.FALSE;
    ValueExpression ve =
      context.getApplication().getExpressionFactory().createValueExpression(
          escape, Boolean.class);
    UIOutput out = new UIOutput();

    out.setValueExpression("escape", ve);
    out.setValue(value);

    return out;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param component
   *
   * @return
   */
  protected Map<String, ?> getParameters(UIComponent component)
  {
    Map<String, Object> map = new HashMap<String, Object>();
    List<UIComponent> children = component.getChildren();

    if (children != null)
    {
      for (UIComponent child : children)
      {
        if (child instanceof UIParameter)
        {
          UIParameter param = (UIParameter) child;

          map.put(param.getName(), param.getValue());
        }
      }
    }

    return map;
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  protected boolean isBlank(String value)
  {
    return (value == null) || (value.length() == 0);
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   *
   * @return
   */
  protected boolean isRendered(FacesContext context, BaseComponent component)
  {
    boolean result = false;

    if (component.isRendered())
    {
      if (component.getRole() != null)
      {
        if (context.getExternalContext().isUserInRole(component.getRole()))
        {
          result = true;
        }
      }
      else
      {
        result = true;
      }
    }

    return result;
  }
}
