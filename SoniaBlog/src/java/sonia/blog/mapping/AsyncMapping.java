/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
