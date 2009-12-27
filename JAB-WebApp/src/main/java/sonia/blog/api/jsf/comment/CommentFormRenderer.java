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



package sonia.blog.api.jsf.comment;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Entry;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Sebastian Sdorra
 */
public class CommentFormRenderer extends BaseRenderer
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
    CommentFormComponent form = (CommentFormComponent) component;

    if (isRendered(context, form))
    {
      Entry entry = form.getEntry();

      if (entry != null)
      {
        BlogRequest request =
          BlogUtil.getBlogRequest(context.getExternalContext().getRequest());
        LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
        String action = linkBuilder.getRelativeLink(request, "/create-comment");
        String redirectUri = linkBuilder.getRelativeLink(request, entry);
        String id = form.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("form", component);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("method", "post", null);
        writer.writeAttribute("action", action, null);
        writer.startElement("a", null);
        writer.writeAttribute("name", "comment-form", null);
        writer.endElement("a");
        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", "redirect", null);
        writer.writeAttribute("value", redirectUri, null);
        writer.endElement("input");
        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", "entryId", null);
        writer.writeAttribute("value", entry.getId(), null);
        writer.endElement("input");
      }
      else
      {
        throw new IOException("Entry attribute is required");
      }
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
  public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException
  {
    CommentFormComponent form = (CommentFormComponent) component;

    if (isRendered(context, form))
    {
      if (form.getEntry() != null)
      {
        context.getResponseWriter().endElement("form");
      }
    }
  }
}
