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



package sonia.blog.api.jsf.link;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
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
public class LinkRenderer extends BaseRenderer
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
    if (!(component instanceof LinkComponent))
    {
      throw new IllegalArgumentException("wrong type");
    }

    ResponseWriter writer = context.getResponseWriter();
    LinkComponent link = (LinkComponent) component;

    if (isRendered(context, link))
    {
      renderBegin(context, link, writer);
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
    if (!(component instanceof LinkComponent))
    {
      throw new IllegalArgumentException("wrong type");
    }

    LinkComponent link = (LinkComponent) component;

    if (isRendered(context, link) && (link.getValue() == null)
        &&!link.getDisabled())
    {
      ResponseWriter writer = context.getResponseWriter();

      writer.endElement("a");
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param link
   * @param writer
   *
   * @throws IOException
   */
  private void renderBegin(FacesContext context, LinkComponent link,
                           ResponseWriter writer)
          throws IOException
  {
    boolean disabled = link.getDisabled();

    if (!disabled)
    {
      writer.startElement("a", link);
    }
    else
    {
      writer.startElement("span", link);
    }

    if (link.getStyle() != null)
    {
      writer.writeAttribute("style", link.getStyle(), null);
    }

    if (link.getStyleClass() != null)
    {
      writer.writeAttribute("class", link.getStyleClass(), null);
    }

    if (!disabled)
    {
      if (link.getTitle() != null)
      {
        writer.writeAttribute("title", link.getTitle(), null);
      }

      if (link.getTarget() != null)
      {
        writer.writeAttribute("target", link.getTarget(), null);
      }

      BlogRequest request =
        BlogUtil.getBlogRequest(context.getExternalContext().getRequest());
      String uri = "#";
      LinkBuilder builder = BlogContext.getInstance().getLinkBuilder();

      if (link.getHref() != null)
      {
        if (link.getAbsolute())
        {
          uri = builder.buildLink(request, link.getHref());
        }
        else
        {
          uri = builder.getRelativeLink(request, link.getHref());
        }
      }
      else if (link.getObject() != null)
      {
        if (link.getAbsolute())
        {
          uri = builder.buildLink(request, link.getObject());
        }
        else
        {
          uri = builder.getRelativeLink(request, link.getObject());
        }
      }

      writer.writeAttribute("href", uri, null);
    }

    if (link.getValue() != null)
    {
      writer.write(link.getValue());

      if (!disabled)
      {
        writer.endElement("a");
      }
      else
      {
        writer.endElement("span");
      }
    }
  }
}
