/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.PermaObject;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;

/**
 *
 * @author sdorra
 */
public abstract class ScrollableMappingEntry extends AbstractMappingEntry
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   * @param start
   * @param end
   *
   * @return
   */
  public abstract boolean handleMapping(BlogRequest request,
          BlogResponse response, String[] param, int start, int end);

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
    int max = request.getCurrentBlog().getEntriesPerPage();
    int start = 0;
    int page = 0;

    try
    {
      page = Integer.parseInt(request.getParameter("page"));
    }
    catch (NumberFormatException ex)
    {
      logger.log(Level.FINE, null, ex);
    }

    if (page > 0)
    {
      start = page * max;
    }

    return handleMapping(request, response, param, start, start + max);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param builder
   * @param object
   *
   * @return
   */
  public String getUri(BlogRequest request, LinkBuilder builder,
                       PermaObject object)
  {
    String result = null;
    String uri = request.getRequestURI();
    int index = uri.lastIndexOf("/");

    result = uri.substring(0, index) + "/" + object.getId() + ".jab";

    return result;
  }
}
