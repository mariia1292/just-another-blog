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
import sonia.blog.api.app.Context;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingConfig;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

import java.io.IOException;
import java.io.PrintWriter;

import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(cacheable = true, compressable = true)
public class FeedMapping extends FinalMapping
{

  /** Field description */
  private static final String FEED_CATEGORY = "category";

  /** Field description */
  private static final String FEED_COMMENT = "comments";

  /** Field description */
  private static final String FEED_DEFAULT = "index";

  /** Field description */
  private static final String FEED_TAG = "tag";

  /** Field description */
  private static final String TYPE_ATOM = "atom";

  /** Field description */
  private static final String TYPE_RSS = "rss";

  /** Field description */
  private static Logger logger = Logger.getLogger(FeedMapping.class.getName());

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
    if ((param != null) && (param.length > 1))
    {
      WireFeed wireFeed = null;
      String type = param[0];

      if (TYPE_ATOM.equals(type))
      {
        wireFeed = new Feed("atom_1.0");
        response.setContentType("application/atom+xml; charset=utf-8");
      }
      else if (TYPE_RSS.equals(type))
      {
        wireFeed = new Channel("rss_2.0");
        response.setContentType("application/rss+xml; charset=utf-8");
      }

      if (wireFeed != null)
      {
        SyndFeed feed = new SyndFeedImpl(wireFeed);

        fillFeed(request, feed);

        String query = param[1];
        String parameter = (param.length > 2)
                           ? param[2]
                           : null;

        appendItems(request, response, feed, query, parameter);

        PrintWriter writer = response.getWriter();

        try
        {
          new SyndFeedOutput().output(feed, writer, true);
        }
        catch (FeedException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
        finally
        {
          writer.close();
        }
      }
      else
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
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
   * @param feed
   * @param queryType
   * @param parameter
   *
   *
   * @throws IOException
   * @throws MalformedURLException
   */
  private void appendItems(BlogRequest request, BlogResponse response,
                           SyndFeed feed, String queryType, String parameter)
          throws MalformedURLException, IOException
  {
    Blog blog = request.getCurrentBlog();
    int maxItems = blog.getEntriesPerPage();
    Long id = null;

    if (Util.hasContent(parameter))
    {
      try
      {
        id = Long.parseLong(parameter);
      }
      catch (NumberFormatException ex)
      {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    }

    if (FEED_DEFAULT.equals(queryType))
    {
      feed.setEntries(buildItems(request, blog, maxItems));
    }
    else if (FEED_COMMENT.equals(queryType))
    {
      feed.setEntries(buildCommentItems(request, response, id, maxItems));
    }
    else if (FEED_CATEGORY.equals(queryType) && (id != null))
    {
      feed.setEntries(buildCategoryItems(request, response, blog, id,
                                         maxItems));
    }
    else if (FEED_TAG.equals(queryType) && (id != null))
    {
      feed.setEntries(buildTagItems(request, response, id, maxItems));
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
   * @param id
   * @param max
   *
   * @return
   *
   * @throws IOException
   */
  private List<SyndEntry> buildCategoryItems(BlogRequest request,
          BlogResponse response, Blog blog, Long id, int max)
          throws IOException
  {
    List<SyndEntry> items = null;
    DAOFactory factory = BlogContext.getDAOFactory();
    CategoryDAO categoryDAO = factory.getCategoryDAO();
    Category category = categoryDAO.get(id);

    if ((category != null) && category.getBlog().equals(blog))
    {
      EntryDAO entryDAO = factory.getEntryDAO();
      List<Entry> entries = entryDAO.getAll(category, 0, max);

      items = buildEntryItems(request, entries);
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
   * @param response
   * @param id
   * @param max
   *
   * @return
   *
   * @throws IOException
   */
  private List<SyndEntry> buildCommentItems(BlogRequest request,
          BlogResponse response, Long id, int max)
          throws IOException
  {
    List<SyndEntry> items = null;
    DAOFactory factory = BlogContext.getDAOFactory();
    EntryDAO entryDAO = factory.getEntryDAO();
    CommentDAO commentDAO = factory.getCommentDAO();
    List<Comment> comments = null;

    if (id != null)
    {
      Entry entry = entryDAO.get(id);

      if ((entry != null) && entry.isPublished())
      {
        comments = commentDAO.getAll(entry, false, 0, max);
      }
      else
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }
    else
    {
      Blog blog = request.getCurrentBlog();

      comments = commentDAO.getAll(blog, 0, max);
    }

    if ((comments != null) &&!comments.isEmpty())
    {
      items = buildCommentItems(request, comments);
    }
    else
    {
      items = new ArrayList<SyndEntry>();
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param comments
   *
   * @return
   *
   * @throws MalformedURLException
   */
  private List<SyndEntry> buildCommentItems(BlogRequest request,
          List<Comment> comments)
          throws MalformedURLException
  {
    List<SyndEntry> items = new ArrayList<SyndEntry>();

    for (Comment comment : comments)
    {
      Entry entry = comment.getEntry();
      StringBuffer link = new StringBuffer(linkBuilder.buildLink(request,
                            entry));

      link.append("#").append(comment.getId());

      SyndEntry item = new SyndEntryImpl();

      item.setAuthor(comment.getAuthorName());
      item.setLink(link.toString());
      item.setPublishedDate(comment.getCreationDate());
      item.setTitle(
          new StringBuffer("RE: ").append(entry.getTitle()).toString());

      List<SyndContent> contents = new ArrayList<SyndContent>();
      SyndContent content = new SyndContentImpl();

      content.setValue(comment.getContent());
      content.setType("text/html");
      contents.add(content);
      item.setContents(contents);
      items.add(item);
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param entries
   *
   * @return
   *
   * @throws MalformedURLException
   */
  private List<SyndEntry> buildEntryItems(BlogRequest request,
          List<Entry> entries)
          throws MalformedURLException
  {
    List<SyndEntry> items = new ArrayList<SyndEntry>();

    for (Entry entry : entries)
    {
      String description = Util.isBlank(entry.getTeaser())
                           ? entry.getContent()
                           : entry.getTeaser();
      List<SyndContent> contents = new ArrayList<SyndContent>();
      SyndContent content = new SyndContentImpl();

      content.setType("text/html");
      content.setValue(description);
      contents.add(content);

      String uri = linkBuilder.buildLink(request, entry);
      SyndEntry item = new SyndEntryImpl();

      item.setAuthor(entry.getAuthorName());
      item.setLink(uri);
      item.setPublishedDate(entry.getPublishingDate());
      item.setContents(contents);
      item.setTitle(entry.getTitle());

      List<SyndCategory> feedCategories = new ArrayList<SyndCategory>();
      List<Category> categories = entry.getCategories();

      for (Category category : categories)
      {
        SyndCategory cat = new SyndCategoryImpl();

        cat.setName(category.getName());
        cat.setTaxonomyUri(linkBuilder.buildLink(request, category));
        feedCategories.add(cat);
      }

      item.setCategories(feedCategories);
      item.setUpdatedDate(entry.getLastUpdate());
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
   * @param max
   *
   * @return
   *
   * @throws MalformedURLException
   */
  private List<SyndEntry> buildItems(BlogRequest request, Blog blog, int max)
          throws MalformedURLException
  {
    List<SyndEntry> items = null;
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    List<Entry> entries = entryDAO.getAll(blog, true, 0, max);

    items = buildEntryItems(request, entries);

    return items;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param id
   * @param max
   *
   * @return
   *
   * @throws IOException
   */
  private List<SyndEntry> buildTagItems(BlogRequest request,
          BlogResponse response, Long id, int max)
          throws IOException
  {
    List<SyndEntry> items = null;
    DAOFactory factory = BlogContext.getDAOFactory();
    TagDAO tagDAO = factory.getTagDAO();
    Tag tag = tagDAO.get(id);

    if (tag != null)
    {
      Blog blog = request.getCurrentBlog();
      EntryDAO entryDAO = factory.getEntryDAO();
      List<Entry> entries = entryDAO.getAll(blog, tag, true, 0, max);

      items = buildEntryItems(request, entries);
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
   * @param feed
   */
  private void fillFeed(BlogRequest request, SyndFeed feed)
  {
    Blog blog = request.getCurrentBlog();
    String link = linkBuilder.buildLink(request, blog);

    feed.setTitle(blog.getTitle());
    feed.setDescription(blog.getDescription());
    feed.setPublishedDate(blog.getCreationDate());
    feed.setLink(link);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;
}
