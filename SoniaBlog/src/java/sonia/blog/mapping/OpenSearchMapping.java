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



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingConfig;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(cacheable = true, compressable = true)
public class OpenSearchMapping extends FinalMapping
{

  /** Field description */
  private static final String MIMETYPE = "text/xml";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    Blog blog = request.getCurrentBlog();
    String link = BlogContext.getInstance().getLinkBuilder().buildLink(request,
                    blog);

    response.setContentType(MIMETYPE);

    PrintWriter writer = null;

    try
    {
      writer = response.getWriter();
      writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      writer.print("<OpenSearchDescription ");
      writer.print("xmlns=\"http://a9.com/-/spec/opensearch/1.1/\" ");
      writer.print("xmlns:moz=\"http://www.mozilla.org/2006/browser/search/\"");
      writer.println(">");
      writer.println("\t<ShortName>JAB - " + blog.getTitle() + "</ShortName>");
      writer.println("\t<Description>" + blog.getDescription()
                     + "</Description>");
      writer.println("\t<Url type=\"text/html\" template=\"" + link
                     + "search.jab?search={searchTerms}\" />");
      writer.println("\t<Query role=\"example\" searchTerms=\"jab\" />");
      writer.println("\t<InputEncoding>UTF-8</InputEncoding>");
      writer.println("\t<Image width=\"16\" height=\"16\">");
      writer.println("\t\t" + link + "resources/images/icons/jab/icon-16.gif");
      writer.println("\t</Image>");
      writer.println("\t<Image width=\"32\" height=\"32\">");
      writer.println("\t\t" + link + "resources/images/icons/jab/icon-32.gif");
      writer.println("\t</Image>");
      writer.println("\t<Image width=\"64\" height=\"64\">");
      writer.println("\t\t" + link + "resources/images/icons/jab/icon-64.gif");
      writer.println("\t</Image>");
      writer.println("\t<Image width=\"128\" height=\"128\">");
      writer.println("\t\t" + link + "resources/images/icons/jab/icon-128.gif");
      writer.println("\t</Image>");
      writer.println("\t<moz:SearchForm>" + link + "</moz:SearchForm>");
      writer.println("</OpenSearchDescription>");
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }
  }
}
