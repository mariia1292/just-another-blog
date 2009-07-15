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


package sonia.blog.api.jsf.select;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.util.SelectAction;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseRenderer;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.text.MessageFormat;

import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Sebastian Sdorra
 */
public class SelectActionRenderer extends BaseRenderer
{

  /** Field description */
  private static final String OPTION = "this.options[this.selectedIndex].value";

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
  public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException
  {
    SelectActionComponent cmp = null;

    if (!(component instanceof SelectActionComponent))
    {
      throw new IllegalArgumentException(
          "component is not an instance of "
          + SelectActionComponent.class.getName());
    }

    cmp = (SelectActionComponent) component;

    if (isRendered(context, cmp))
    {
      Map<String, ?> parameter = getParameters(component);
      Object requestObject = context.getExternalContext().getRequest();
      BlogRequest request = BlogUtil.getBlogRequest(requestObject);
      List<SelectAction> actions = cmp.getActions();
      ResponseWriter writer = context.getResponseWriter();

      writer.startElement("select", cmp);

      if (!Util.isBlank(cmp.getOnchange()))
      {
        String out = MessageFormat.format(cmp.getOnchange(), OPTION);

        writer.writeAttribute("onchange", out, null);
      }

      writer.startElement("option", cmp);
      writer.writeAttribute("value", "---", null);
      writer.write("---");
      writer.endElement("option");

      for (SelectAction action : actions)
      {
        writer.startElement("option", cmp);
        writer.writeAttribute("value", action.getOutput(request, parameter),
                              null);
        writer.writeText(action.getLable(), null);
        writer.endElement("option");
      }

      writer.endElement("select");
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public boolean getRendersChildren()
  {
    return true;
  }
}