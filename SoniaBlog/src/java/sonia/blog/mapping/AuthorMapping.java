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
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.api.mapping.ScrollableFilterMapping;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;
import sonia.blog.entity.User;
import sonia.blog.util.BlogUtil;
import sonia.blog.wui.BlogBean;

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
public class AuthorMapping extends ScrollableFilterMapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AuthorMapping.class.getName());

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
   * @param max
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected String handleScrollableFilterMapping(BlogRequest request,
          BlogResponse response, String[] param, int start, int max)
          throws IOException, ServletException
  {
    String result = null;

    if ((param != null) && (param.length > 0))
    {
      try
      {
        Long userId = Long.parseLong(param[0]);
        User author = userDAO.get(userId);
        Blog blog = request.getCurrentBlog();

        if (author != null)
        {
          if (param.length > 1)
          {
            Long entryId = Long.parseLong(param[1]);

            result = handleDetailView(request, blog, author, entryId);
          }
          else
          {
            result = handleListView(request, blog, author, start, max);
          }
        }
      }
      catch (NumberFormatException ex)
      {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param blog
   * @param author
   * @param entryId
   *
   * @return
   */
  private String handleDetailView(BlogRequest request, Blog blog, User author,
                                  Long entryId)
  {
    String result = null;
    Entry entry = entryDAO.get(entryId);

    if ((entry != null) && blog.equals(entry.getBlog())
        && (entry.getAuthor() != null) && entry.getAuthor().equals(author))
    {
      if (logger.isLoggable(Level.FINER))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("set entry(").append(entryId).append(") to BlogBean");
        logger.finer(msg.toString());
      }

      BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                            BlogBean.NAME);

      setDisplayContent(request, entry, false);
      blogBean.setEntry(entry);

      Entry prev = entryDAO.getPreviousEntry(blog, author, entry, true);
      Entry next = entryDAO.getNextEntry(blog, author, entry, true);
      StringBuffer prefix = new StringBuffer("/author/");

      prefix.append(author.getId()).append("/");

      LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
      String prevUri = null;
      String nextUri = null;

      if (prev != null)
      {
        StringBuffer prevBuffer = new StringBuffer(prefix);

        prevUri = prevBuffer.append(prev.getId()).append(".jab").toString();
        prevUri = linkBuilder.buildLink(request, prevUri);
      }

      if (next != null)
      {
        StringBuffer nextBuffer = new StringBuffer(prefix);

        nextUri = nextBuffer.append(next.getId()).append(".jab").toString();
        nextUri = linkBuilder.buildLink(request, nextUri);
      }

      navigation = new SimpleMappingNavigation(prevUri, nextUri);
      result = buildTemplateViewId(request, Constants.TEMPLATE_DETAIL);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param blog
   * @param author
   * @param start
   * @param max
   *
   * @return
   */
  private String handleListView(BlogRequest request, Blog blog, User author,
                                int start, int max)
  {
    List<Entry> entries = entryDAO.getAllByAuthor(blog, author, true, start,
                            max);
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

    if ((entries != null) && (size > max))
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

    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String link = linkBuilder.buildLink(request,
                    "/author/" + author.getId() + "/");

    navigation = new SimpleMappingNavigation(prevUri, nextUri,
            link + "{0,number,#}.jab");

    return buildTemplateViewId(blog, Constants.TEMPLATE_LIST);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private EntryDAO entryDAO;

  /** Field description */
  private MappingNavigation navigation;

  /** Field description */
  @Dao
  private UserDAO userDAO;
}
