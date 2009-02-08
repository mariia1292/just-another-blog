/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

/**
 *
 * @author sdorra
 */
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
      writer.println("\t\t" + link + "template/jab/images/icon-16.gif");
      writer.println("\t</Image>");
      writer.println("\t<Image width=\"32\" height=\"32\">");
      writer.println("\\tt" + link + "template/jab/images/icon-32.gif");
      writer.println("\t</Image>");
      writer.println("\t<Image width=\"64\" height=\"64\">");
      writer.println("\t\t" + link + "template/jab/images/icon-64.gif");
      writer.println("\t</Image>");
      writer.println("\t<Image width=\"128\" height=\"128\">");
      writer.println("\t\t" + link + "template/jab/images/icon-128.gif");
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
