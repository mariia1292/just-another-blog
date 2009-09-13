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
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;

import sonia.cache.Cacheable;

import sonia.rss.Channel;
import sonia.rss.FeedParser;
import sonia.rss.Item;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@Cacheable
public class FeedMapping extends FinalMapping
{

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
    if ((param != null) && (param.length > 1))
    {
      String type = param[param.length - 1];

      if (type == null)
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }

      FeedParser parser = FeedParser.getInstance(type);

      if (parser == null)
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }

      LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
      Blog blog = request.getCurrentBlog();
      List<Item> items = null;
      int max = blog.getEntriesPerPage();

      try
      {
        if (param[0].equals("index"))
        {
          items = buildItems(request, blog, linkBuilder, max);
        }
        else if (param[0].equals("category"))
        {
          Long id = Long.parseLong(param[1]);

          items = buildCategoryItems(request, response, blog, linkBuilder, id,
                                     max);
        }
        else if (param[0].equals("tag"))
        {
          Long id = Long.parseLong(param[1]);

          items = buildTagItems(request, response, linkBuilder, id, max);
        }
        else if (param[0].equals("comment"))
        {
          if (!Util.isBlank(param[1]) &&!param[1].equals("index"))
          {
            Long id = Long.parseLong(param[1]);

            items = buildCommentItems(request, response, linkBuilder, id, max);
          }
          else
          {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
          }
        }
        else if (param[0].equals("comments"))
        {
          items = buildCommentItems(request, response, linkBuilder, null, max);
        }
      }
      catch (NumberFormatException ex)
      {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }

      Channel channel = buildChannel(request, linkBuilder, blog, items);

      printChannel(response, parser, channel);
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param blog
   * @param linkBuilder
   * @param id
   * @param max
   *
   * @return
   *
   * @throws IOException
   */
  private List<Item> buildCategoryItems(BlogRequest request,
          BlogResponse response, Blog blog, LinkBuilder linkBuilder, Long id,
          int max)
          throws IOException
  {
    List<Item> items = null;
    DAOFactory factory = BlogContext.getDAOFactory();
    CategoryDAO categoryDAO = factory.getCategoryDAO();
    Category category = categoryDAO.get(id);

    if ((category != null) && category.getBlog().equals(blog))
    {
      EntryDAO entryDAO = factory.getEntryDAO();
      List<Entry> entries = entryDAO.findAllByCategory(category, 0, max);

      items = buildEntryItems(request, linkBuilder, entries);
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBuilder
   * @param blog
   * @param items
   *
   * @return
   *
   * @throws MalformedURLException
   */
  private Channel buildChannel(BlogRequest request, LinkBuilder linkBuilder,
                               Blog blog, List<Item> items)
          throws MalformedURLException
  {
    String link = linkBuilder.buildLink(request, blog);
    Channel channel = new Channel(blog.getTitle(), new URL(link),
                                  blog.getDescription());

    channel.setPubDate(blog.getCreationDate());

    if ((items != null) &&!items.isEmpty())
    {
      channel.setItems(items);
    }

    return channel;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param linkBuilder
   * @param id
   * @param max
   *
   * @return
   *
   * @throws IOException
   */
  private List<Item> buildCommentItems(BlogRequest request,
          BlogResponse response, LinkBuilder linkBuilder, Long id, int max)
          throws IOException
  {
    List<Item> items = null;
    DAOFactory factory = BlogContext.getDAOFactory();
    EntryDAO entryDAO = factory.getEntryDAO();
    CommentDAO commentDAO = factory.getCommentDAO();
    List<Comment> comments = null;

    if (id != null)
    {
      Entry entry = entryDAO.get(id);

      if ((entry != null) && entry.isPublished())
      {
        comments = commentDAO.findAllActivesByEntry(entry, 0, max);
      }
      else
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }
    else
    {
      Blog blog = request.getCurrentBlog();

      comments = commentDAO.findAllByBlog(blog, 0, max);
    }

    if ((comments != null) &&!comments.isEmpty())
    {
      items = buildCommentItems(request, linkBuilder, comments);
    }
    else
    {
      items = new ArrayList<Item>();
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBuilder
   * @param comments
   *
   * @return
   *
   * @throws MalformedURLException
   */
  private List<Item> buildCommentItems(BlogRequest request,
          LinkBuilder linkBuilder, List<Comment> comments)
          throws MalformedURLException
  {
    List<Item> items = new ArrayList<Item>();

    for (Comment comment : comments)
    {
      Entry entry = comment.getEntry();
      String link = linkBuilder.buildLink(request, entry);
      Item item = new Item(entry.getTitle(), new URL(link),
                           comment.getContent());

      item.setAuthor(comment.getAuthorName());
      item.setPubDate(comment.getCreationDate());
      items.add(item);
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBuilder
   * @param entries
   *
   * @return
   *
   * @throws MalformedURLException
   */
  private List<Item> buildEntryItems(BlogRequest request,
                                     LinkBuilder linkBuilder,
                                     List<Entry> entries)
          throws MalformedURLException
  {
    List<Item> items = new ArrayList<Item>();

    for (Entry entry : entries)
    {
      String description = Util.isBlank(entry.getTeaser())
                           ? entry.getContent()
                           : entry.getTeaser();
      String link = linkBuilder.buildLink(request, entry);
      Item item = new Item(entry.getTitle(), new URL(link), description);

      item.setAuthor(entry.getAuthorName());
      item.setPubDate(entry.getCreationDate());
      items.add(item);
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param blog
   * @param linkBuilder
   * @param max
   *
   * @return
   *
   * @throws MalformedURLException
   */
  private List<Item> buildItems(BlogRequest request, Blog blog,
                                LinkBuilder linkBuilder, int max)
          throws MalformedURLException
  {
    List<Item> items = null;
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    List<Entry> entries = entryDAO.findAllActivesByBlog(blog, 0, max);

    items = buildEntryItems(request, linkBuilder, entries);

    return items;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param linkBuilder
   * @param id
   * @param max
   *
   * @return
   *
   * @throws IOException
   */
  private List<Item> buildTagItems(BlogRequest request, BlogResponse response,
                                   LinkBuilder linkBuilder, Long id, int max)
          throws IOException
  {
    List<Item> items = null;
    DAOFactory factory = BlogContext.getDAOFactory();
    TagDAO tagDAO = factory.getTagDAO();
    Tag tag = tagDAO.get(id);

    if (tag != null)
    {
      Blog blog = request.getCurrentBlog();
      EntryDAO entryDAO = factory.getEntryDAO();
      List<Entry> entries = entryDAO.findAllByBlogAndTag(blog, tag, 0, max);

      items = buildEntryItems(request, linkBuilder, entries);
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param parser
   * @param channel
   *
   * @throws IOException
   */
  private void printChannel(BlogResponse response, FeedParser parser,
                            Channel channel)
          throws IOException
  {
    response.setContentType(parser.getMimeType());

    ServletOutputStream out = response.getOutputStream();

    try
    {
      parser.store(channel, out);
    }
    finally
    {
      if (out != null)
      {
        out.close();
      }
    }
  }
}
