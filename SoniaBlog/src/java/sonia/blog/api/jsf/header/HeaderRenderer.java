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



package sonia.blog.api.jsf.header;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.macro.WebResource;
import sonia.blog.entity.Blog;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseRenderer;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.text.MessageFormat;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Sebastian Sdorra
 */
public class HeaderRenderer extends BaseRenderer
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
    if (!(component instanceof HeaderComponent))
    {
      throw new IllegalArgumentException();
    }

    if (BlogContext.getInstance().isInstalled())
    {
      HeaderComponent header = (HeaderComponent) component;
      ResponseWriter writer = context.getResponseWriter();

      if (header.getComments())
      {
        writer.writeComment("start header");
      }

      List<WebResource> resources = header.getResources(context);
      BlogRequest request =
        BlogUtil.getBlogRequest(context.getExternalContext().getRequest());
      Blog blog = request.getCurrentBlog();

      if (Util.hasContent(resources))
      {
        for (WebResource resource : resources)
        {
          String html = resource.toHTML();

          writer.write(MessageFormat.format(html, blog.getId(),
                                            blog.getIdentifier(),
                                            blog.getTitle(),
                                            blog.getDescription()));
          writer.write("\n");
        }
      }

      if (header.getComments())
      {
        writer.writeComment("end header");
      }
    }
  }
}
