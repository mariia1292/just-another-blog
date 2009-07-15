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


package sonia.blog.api.jsf.trackback;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
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
public class TrackBackRenderer extends BaseRenderer
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
    TrackBackComponent tbc = (TrackBackComponent) component;
    ContentObject co = tbc.getObject();

    if (co.getId() != null)
    {
      BlogContext ctx = BlogContext.getInstance();
      LinkBuilder builder = ctx.getLinkBuilder();
      BlogRequest request =
        BlogUtil.getBlogRequest(context.getExternalContext().getRequest());
      Blog blog = request.getCurrentBlog();
      String linkBase = builder.buildLink(request, "/");
      StringBuffer linkBuffer = new StringBuffer();

      linkBuffer.append(linkBase).append("list/");
      linkBuffer.append(co.getId()).append(".jab");

      String link = linkBuffer.toString();
      StringBuffer tbLinkBuffer = new StringBuffer();

      tbLinkBuffer.append(linkBase).append("trackback/").append(co.getId());

      String tbLink = tbLinkBuffer.toString();
      String date = blog.getDateFormatter().format(co.getCreationDate());
      ResponseWriter writer = context.getResponseWriter();

      writer.write("<!--\n");
      writer.write("<rdf:RDF xmlns:rdf=\"");
      writer.write("http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
      writer.write("  xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n");
      writer.write("  xmlns:trackback=\"");
      writer.write(
          "http://madskills.com/public/xml/rss/module/trackback/\">\n");

      // start description
      writer.write("<rdf:Description\n");

      // about
      writer.write("  rdf:about=\"");
      writer.write(link);
      writer.write("\"\n");

      // identifier
      writer.write("  dc:identifier=\"");
      writer.write(link);
      writer.write("\"\n");

      // TrackBackUrl
      writer.write("  trackback:ping=\"");
      writer.write(tbLink);
      writer.write("\"\n");

      // title
      writer.write("  dc:title=\"");
      writer.write(co.getTitle());
      writer.write("\"\n");

      // creator
      writer.write("  dc:creator=\"");
      writer.write(co.getAuthorName());
      writer.write("\"\n");

      // date
      writer.write("  dc:date=\"");
      writer.write(date);
      writer.write("\" />\n");

      // end description
      writer.write("</rdf:RDF>\n");
      writer.write("-->\n");
    }
  }
}