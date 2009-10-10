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
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.mapping.FilterMapping;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Page;
import sonia.blog.wui.PageBean;

import sonia.cache.Cacheable;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@Cacheable({ "user" })
public class PageMapping extends FilterMapping
{

  /** Field description */
  private static Logger logger = Logger.getLogger(PageMapping.class.getName());

  //~--- get methods ----------------------------------------------------------

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
          else
          {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
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

    setDisplayContent(request, page, false);
    pageBean.setPage(page);
    request.setAttribute(PageBean.NAME, pageBean);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private PageDAO pageDAO;
}
