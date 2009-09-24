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



package sonia.blog.api.jsf.messages;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseRenderer;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Sebastian Sdorra
 */
public class MessagesRenderer extends BaseRenderer
{

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
    MessagesComponent msgCmp = (MessagesComponent) component;

    if (isRendered(context, msgCmp))
    {
      if (link == null)
      {
        BlogRequest request =
          BlogUtil.getBlogRequest(context.getExternalContext().getRequest());

        link =
          BlogContext.getInstance().getLinkBuilder().getRelativeLink(request,
            "/async/messages.json");
      }

      String clientId = component.getClientId(context);
      ResponseWriter writer = context.getResponseWriter();

      writer.startElement("span", component);
      writer.writeAttribute("id", clientId, null);
      writer.endElement("span");
      writer.startElement("script", component);
      writer.writeAttribute("type", "text/javascript", null);
      writer.append("$(document).ready(function(){");
      writer.append("$(\"span#").append(clientId).append("\").messages(\"");
      writer.append(link);
      writer.append("\", {");

      boolean first = true;
      String infoClass = msgCmp.getInfoClass();

      if (Util.hasContent(infoClass))
      {
        writer.append("\"infoClass\": \"").append(infoClass).append("\"");
        first = false;
      }

      String warnClass = msgCmp.getWarnClass();

      if (Util.hasContent(warnClass))
      {
        if (!first)
        {
          writer.append(",");
        }

        writer.append("\"warnClass\": \"").append(warnClass).append("\"");
        first = false;
      }

      String errorClass = msgCmp.getErrorClass();

      if (Util.hasContent(errorClass))
      {
        if (!first)
        {
          writer.append(",");
        }

        writer.append("\"errorClass\": \"").append(errorClass).append("\"");
        first = false;
      }

      String fatalClass = msgCmp.getFatalClass();

      if (Util.hasContent(fatalClass))
      {
        if (!first)
        {
          writer.append(",");
        }

        writer.append("\"fatalClass\": \"").append(fatalClass).append("\"");
        first = false;
      }

      String styleClass = msgCmp.getStyleClass();

      if (Util.hasContent(styleClass))
      {
        if (!first)
        {
          writer.append(",");
        }

        writer.append("\"styleClass\": \"").append(styleClass).append("\"");
        first = false;
      }

      String style = msgCmp.getStyle();
      if ( Util.hasContent(style) ){
        if ( !first )
        {
          writer.append( "," );
        }
        writer.append("\"style\": \"").append(style).append("\"");
      }

      writer.append("});");
      writer.append("});");
      writer.endElement("script");
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String link;
}
