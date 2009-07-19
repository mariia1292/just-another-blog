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



package sonia.blog.api.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.entity.Blog;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class ScrollableFilterMapping extends FilterMapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ScrollableFilterMapping.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   * @param start
   * @param max
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  protected abstract String handleScrollableFilterMapping(BlogRequest request,
          BlogResponse response, String[] param, int start, int max)
          throws IOException, ServletException;

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
    String result = null;
    Blog blog = request.getCurrentBlog();

    if (blog != null)
    {
      int max = blog.getEntriesPerPage();
      int start = 0;
      int page = 0;
      String pageParam = request.getParameter("page");

      if (!Util.isBlank(pageParam))
      {
        try
        {
          page = Integer.parseInt(pageParam);
        }
        catch (NumberFormatException ex)
        {
          logger.log(Level.FINE, null, ex);
        }
      }

      if (page > 0)
      {
        start = page * max;
      }

      result = handleScrollableFilterMapping(request, response, param, start,
              max);
    }
    else
    {
      logger.severe("blog is null, sending '500 internal server error'");
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  protected int getCurrentPage(BlogRequest request)
  {
    int page = 0;
    String param = request.getParameter("page");

    if (param != null)
    {
      try
      {
        page = Integer.parseInt(param);
      }
      catch (NumberFormatException ex) {}
    }

    return page;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param page
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  protected String getPageUri(BlogRequest request, int page)
  {
    String uri =
      request.getRequestURI().substring(request.getContextPath().length())
      + "?page=" + page;
    Enumeration<String> enm = request.getParameterNames();

    while (enm.hasMoreElements())
    {
      String name = enm.nextElement();

      if (!name.equals("page"))
      {
        uri += "&" + name + "=" + request.getParameter(name);
      }
    }

    return uri;
  }
}
