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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.entity.Blog;
import sonia.util.Util;

/**
 *
 * @author Sebastian Sdorra
 */
public class AsyncMapping extends FinalMapping
{

  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response, String[] param) throws IOException, ServletException
  {
    System.out.println();
    if ( param != null && param.length > 0 )
    {
      String provider = param[0];
      if ( Util.hasContent(provider) )
      {
        response.setContentType("application/x-javascript");
        if ( provider.equals("search") )
        {
          search( request, response );
        }
      }
      else
      {
        response.sendError( HttpServletResponse.SC_NOT_FOUND );
      }
    }
    else
    {
      response.sendError( HttpServletResponse.SC_NOT_FOUND );
    }
  }

  private void search(BlogRequest request, BlogResponse response) throws IOException
  {
    PrintWriter writer = response.getWriter();
    writer.println("[");
    String query = request.getParameter("query");
    if ( Util.hasContent(query) )
    {
      try
      {
        Blog blog = request.getCurrentBlog();
      SearchContext ctx = BlogContext.getInstance().getSearchContext();
      List<SearchEntry> entries = ctx.search(blog, query);
      if ( Util.hasContent(entries) )
      {
        
        LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
        Iterator<SearchEntry> entryIt = entries.iterator();
        while ( entryIt.hasNext() )
        {
          SearchEntry e = entryIt.next();
          writer.print( "  {" );
          writer.print( " value : '" );
          writer.print( e.getTitle() );
          writer.print( "'," );
          writer.print( " url : '" );
          writer.print( linkBuilder.buildLink(request, e.getData()) );
          writer.print( "'" );
          if ( entryIt.hasNext() )
          {
            writer.println( " }," );
          }
          else
          {
            writer.println( " }" );
          }
        }
      }
      }
      catch ( SearchException ex )
      {
        logger.log( Level.SEVERE, null, ex );
      }
    }
    writer.println("]");
  }

  private static Logger logger = Logger.getLogger( AsyncMapping.class.getName() );

}