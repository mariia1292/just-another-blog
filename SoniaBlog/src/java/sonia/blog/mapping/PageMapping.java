/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.mapping.FilterMapping;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.entity.Page;
import sonia.blog.wui.PageBean;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.logging.Level;

import javax.servlet.ServletException;
import sonia.blog.entity.Blog;

/**
 *
 * @author sdorra
 */
public class PageMapping extends FilterMapping
{

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingNavigation getMappingNavigation()
  {
    return null;
  }

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
  @Override
  protected String handleFilterMapping(BlogRequest request,
          BlogResponse response, String[] param)
          throws IOException, ServletException
  {
    Blog blog = request.getCurrentBlog();
    String result = Constants.TEMPLATE_ERROR;

    if ((param != null) && (param.length > 0))
    {
      String idString = param[0];

      if (Util.hasContent(idString))
      {
        try
        {
          long id = Long.parseLong(idString);
          Page page = pageDAO.get(id, blog, true);

          if (page != null)
          {
            setPage(request, page);
            result = Constants.TEMPLATE_PAGE;
          }
        }
        catch (NumberFormatException ex)
        {
          logger.log(Level.FINEST, null, ex);
        }
      }
    }

    return buildTemplateViewId(blog, result);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param page
   */
  private void setPage(BlogRequest request, Page page)
  {
    if (logger.isLoggable(Level.FINER))
    {
      StringBuffer log = new StringBuffer();

      log.append("creating PageBean with page ").append(page.getId());
      logger.finer(log.toString());
    }

    PageBean pageBean = new PageBean();

    pageBean.setPage(page);
    request.setAttribute(PageBean.NAME, pageBean);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private PageDAO pageDAO;
}
