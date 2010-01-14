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

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Context;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingConfig;
import sonia.blog.api.search.SearchCategory;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.entity.Blog;

import sonia.util.Util;

import sonia.web.io.JSONWriter;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(cacheable = true, compressable = true)
public class QuickSearchMapping extends FinalMapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(QuickSearchMapping.class.getName());

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
    JSONWriter writer = new JSONWriter(response.getWriter());

    try
    {
      response.setContentType("application/x-javascript");
      writer.startArray();

      String query = request.getParameter("query");

      if (Util.hasContent(query))
      {
        try
        {
          Blog blog = request.getCurrentBlog();
          Collection<SearchCategory> categories = ctx.search(blog,
                                                    request.getLocale(), query);

          if (Util.hasContent(categories))
          {
            List<SearchEntry> entries = new ArrayList<SearchEntry>();

            for (SearchCategory cat : categories)
            {
              entries.addAll(cat.getEntries());
            }

            if (Util.hasContent(entries))
            {
              Iterator<SearchEntry> entryIt = entries.iterator();

              while (entryIt.hasNext())
              {
                SearchEntry e = entryIt.next();

                writer.startObject();
                writer.write("value", e.getTitle(), false);
                writer.write("url",
                             linkBuilder.getRelativeLink(request, e.getData()),
                             true);
                writer.endObject(!entryIt.hasNext());
              }
            }
          }
        }
        catch (SearchException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }

      writer.endArray(true);
    }
    finally
    {
      writer.close();
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private SearchContext ctx;

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;
}
