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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.api.mapping.ScrollableFilterMapping;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;
import sonia.blog.util.BlogUtil;
import sonia.blog.wui.BlogBean;

import sonia.cache.Cacheable;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.ListDataModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@Cacheable({ "user", "locale" })
public class ListMapping extends ScrollableFilterMapping
{

  /** Field description */
  private static Logger logger = Logger.getLogger(ListMapping.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingNavigation getMappingNavigation()
  {
    return navigation;
  }

  //~--- methods --------------------------------------------------------------

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
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected String handleScrollableFilterMapping(BlogRequest request,
          BlogResponse response, String[] param, int start, int end)
          throws IOException, ServletException
  {
    String result = null;

    if ((param != null) && (param.length > 0))
    {
      String idString = param[0];

      try
      {
        Long id = Long.parseLong(idString);

        result = handleDetailView(request, id);
      }
      catch (NumberFormatException ex)
      {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    }
    else
    {
      result = handleListView(request, start, end);
    }

    if (logger.isLoggable(Level.FINER))
    {
      logger.finer("FilterMapping result is " + result);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param id
   *
   * @return
   *
   * @throws IOException
   */
  private String handleDetailView(BlogRequest request, Long id)
          throws IOException
  {
    String result = null;
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    Entry entry = entryDAO.get(id);

    if ((entry != null) && entry.isPublished())
    {
      if (logger.isLoggable(Level.FINER))
      {
        logger.finer("set entry(" + entry.getId() + ") to BlogBean");
      }

      BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                            BlogBean.NAME);

      setDisplayContent(request, entry, false);
      blogBean.setEntry(entry);

      Blog blog = request.getCurrentBlog();
      Entry next = entryDAO.getNextEntry(blog, entry, true);
      Entry prev = entryDAO.getPreviousEntry(blog, entry, true);
      String nextUri = null;
      String prevUri = null;
      String baseLink = "/list/";

      if (prev != null)
      {
        prevUri = new StringBuilder(baseLink).append(prev.getId()).append(
          ".jab").toString();
      }

      if (next != null)
      {
        nextUri = new StringBuilder(baseLink).append(next.getId()).append(
          ".jab").toString();
      }

      String detailPattern =
        new StringBuilder(baseLink).append("{0,number,#}.jab").toString();

      navigation = new SimpleMappingNavigation(prevUri, nextUri, detailPattern);
      result = buildTemplateViewId(blog, Constants.TEMPLATE_DETAIL);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param start
   * @param max
   *
   * @return
   */
  private String handleListView(BlogRequest request, int start, int max)
  {
    Blog blog = request.getCurrentBlog();
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    List<Entry> entries = entryDAO.findAllActivesByBlog(blog, start, max + 1);
    String prevUri = null;
    String nextUri = null;

    if (start > 0)
    {
      int page = getCurrentPage(request);

      if (page > 0)
      {
        prevUri = getPageUri(request, page - 1);
      }
    }

    int size = entries.size();

    if (size > max)
    {
      int page = getCurrentPage(request);

      nextUri = getPageUri(request, page + 1);
    }
    else if (size < max)
    {
      max = size;
    }

    entries = entries.subList(0, max);

    BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                          BlogBean.NAME);

    if (logger.isLoggable(Level.FINER))
    {
      logger.finer("set entry list(" + entries.size() + ") to BlogBean");
    }

    setDisplayContent(request, entries, true);
    blogBean.setPageEntries(new ListDataModel(entries));

    StringBuffer detailPattern = new StringBuffer("/list/");

    detailPattern.append("{0,number,#}.jab");
    navigation = new SimpleMappingNavigation(prevUri, nextUri,
            detailPattern.toString());

    return buildTemplateViewId(blog, Constants.TEMPLATE_LIST);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private MappingNavigation navigation;
}
