/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.entity.Blog;
import sonia.blog.entity.PermaObject;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;

/**
 *
 * @author sdorra
 */
public class OpenSearchMappingEntry implements MappingEntry
{

  /** Field description */
  private static final String MIMETYPE = "text/xml";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(OpenSearchMappingEntry.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @return
   */
  public boolean handleMapping(BlogRequest request, BlogResponse response,
                               String[] param)
  {
    Blog blog = request.getCurrentBlog();

    response.setContentType(MIMETYPE);

    String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    result += "<OpenSearchDescription ";
    result += "xmlns=\"http://a9.com/-/spec/opensearch/1.1/\" ";
    result += "xmlns:moz=\"http://www.mozilla.org/2006/browser/search/\">\n";
    result += "  <ShortName>" + blog.getTitle() + "</ShortName>\n";
    result += "  <Description>" + blog.getDescription() + "</Description>\n";

    String link = BlogContext.getInstance().getLinkBuilder().buildLink(request,
                    "/");

    result += "  <Url type=\"text/html\" template=\"" + link
              + "search.jab?search={searchTerms}\" />\n";
    result += "  <Query role=\"example\" searchTerms=\"jab\" />\n";
    result += "  <InputEncoding>UTF-8</InputEncoding>\n";
    result += "  <Image width=\"16\" height=\"16\">\n";
    result += "    " + link + "template/jab/images/icon-16.gif\n";
    result += "  </Image>\n";
    result += "  <Image width=\"32\" height=\"32\">\n";
    result += "    " + link + "template/jab/images/icon-32.gif\n";
    result += "  </Image>\n";
    result += "  <Image width=\"64\" height=\"64\">\n";
    result += "    " + link + "template/jab/images/icon-64.gif\n";
    result += "  </Image>\n";
    result += "  <Image width=\"128\" height=\"128\">\n";
    result += "    " + link + "template/jab/images/icon-128.gif\n";
    result += "  </Image>\n";
    result += "  <moz:SearchForm>" + link + "</moz:SearchForm>\n";
    result += "</OpenSearchDescription>";

    ServletOutputStream out = null;

    try
    {
      out = response.getOutputStream();
      out.print(result);
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (out != null)
      {
        try
        {
          out.close();
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }

    return false;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBuilder
   * @param object
   *
   * @return
   */
  public String getUri(BlogRequest request, LinkBuilder linkBuilder,
                       PermaObject object)
  {
    return null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isNavigationRendered()
  {
    return false;
  }
}
