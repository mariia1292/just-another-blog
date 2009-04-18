/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.entity.Blog;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public abstract class FilterMapping implements Mapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(FilterMapping.class.getName());

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
   *
   * @throws IOException
   * @throws ServletException
   */
  protected abstract String handleFilterMapping(BlogRequest request,
          BlogResponse response, String[] param)
          throws IOException, ServletException;

  /**
   * Method description
   *
   *
   * @param request
   * @param resonse
   * @param param
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public boolean handleMapping(BlogRequest request, BlogResponse resonse,
                               String[] param)
          throws IOException, ServletException
  {
    boolean result = false;
    String viewId = handleFilterMapping(request, resonse, param);

    if (Util.isBlank(viewId))
    {
      logger.severe("viewid is null, sending '404 not found'");
      resonse.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    else
    {
      request.setViewId(viewId);
      result = true;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param viewIdSuffix
   *
   * @return
   */
  protected String buildTemplateViewId(BlogRequest request, String viewIdSuffix)
  {
    return buildTemplateViewId(request.getCurrentBlog(), viewIdSuffix);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param viewIdSuffix
   *
   * @return
   */
  protected String buildTemplateViewId(Blog blog, String viewIdSuffix)
  {
    return "/template/" + blog.getTemplate() + "/" + viewIdSuffix;
  }
}
