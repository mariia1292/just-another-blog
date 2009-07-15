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


package sonia.blog.api.jsf.widget;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.lang.StringEscapeUtils;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.macro.browse.BlogMacroWidget;
import sonia.blog.entity.ContentObject;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseRenderer;

import sonia.macro.browse.MacroWidget;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class WidgetRenderer extends BaseRenderer
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(WidgetRenderer.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   *
   * @throws IOException
   */
  @Override
  public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException
  {
    if (!(component instanceof WidgetComponent))
    {
      throw new IllegalArgumentException(
          "component is not an instance of WidgetComponent");
    }

    WidgetComponent widgetComponent = (WidgetComponent) component;
    BlogMacroWidget widget = getWidget(widgetComponent);

    if (widget != null)
    {
      BlogRequest request = getRequest(context);
      ContentObject object = widgetComponent.getObject();
      String name = widgetComponent.getName();
      String param = widgetComponent.getParam();

      if (widgetComponent.getResult())
      {
        String result = widget.getResult(request, object, name, param);

        if (Util.hasContent(result))
        {
          result = escape(result);
          context.getResponseWriter().write(result);
        }
      }
      else
      {
        String formEl = widget.getFormElement(request, object, name, param);

        if (Util.hasContent(formEl))
        {
          context.getResponseWriter().write(formEl);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param result
   *
   * @return
   */
  private String escape(String result)
  {
    return StringEscapeUtils.escapeHtml(result).replace(" ",
            "&nbsp;").replace("'", "&#039;").replace("\n", "<br />").replace("\r", "");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  private BlogRequest getRequest(FacesContext context)
  {
    return BlogUtil.getBlogRequest(context.getExternalContext().getRequest());
  }

  /**
   * Method description
   *
   *
   * @param component
   *
   * @return
   */
  private BlogMacroWidget getWidget(WidgetComponent component)
  {
    BlogMacroWidget widget = null;
    Class<? extends MacroWidget> macroClass = component.getWidget();

    if ((macroClass != null)
        && BlogMacroWidget.class.isAssignableFrom(macroClass))
    {
      try
      {
        widget = (BlogMacroWidget) macroClass.newInstance();
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return widget;
  }
}