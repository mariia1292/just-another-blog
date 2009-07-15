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


package sonia.blog.api.jsf.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Sebastian Sdorra
 */
public class SpamInputRenderer extends BaseRenderer
{

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   */
  @Override
  public void decode(FacesContext context, UIComponent component)
  {
    if (!(component instanceof SpamInputComponent))
    {
      throw new IllegalArgumentException(
          "component is not an instance of SpamComponent");
    }

    SpamInputComponent sc = (SpamInputComponent) component;

    if (sc.isRendered())
    {
      String clientId = component.getClientId(context);
      String input =
        context.getExternalContext().getRequestParameterMap().get(clientId);
      String answer = (String) context.getExternalContext().getSessionMap().get(
                          SpamLabelRenderer.REQUESTKEY);

      context.getExternalContext().getSessionMap().remove(
          SpamLabelRenderer.REQUESTKEY);

      boolean valid = input.equals(answer);

      if (!valid)
      {
        String msg = context.getApplication().getResourceBundle(context,
                       "message").getString("spamInputFailure");

        context.addMessage(clientId,
                           new FacesMessage(FacesMessage.SEVERITY_WARN, msg,
                             null));
      }

      sc.setValid(valid);
    }
  }

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
    if (!(component instanceof SpamInputComponent))
    {
      throw new IllegalArgumentException(
          "component is not an instance of SpamComponent");
    }

    SpamInputComponent sc = (SpamInputComponent) component;

    if (sc.isRendered())
    {
      String clientId = sc.getClientId(context);
      ResponseWriter writer = context.getResponseWriter();

      writer.startElement("input", component);
      writer.writeAttribute("type", "text", null);
      writer.writeAttribute("id", clientId, null);
      writer.writeAttribute("name", clientId, null);
      writer.endElement("input");
    }
  }
}