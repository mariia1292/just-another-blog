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
import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.macro.DefaultWebResources;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingConfig;
import sonia.blog.api.search.SearchCategory;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.entity.Blog;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.feed.module.opensearch.OpenSearchModule;
import com.sun.syndication.feed.module.opensearch.entity.OSQuery;
import com.sun.syndication.feed.module.opensearch.impl.OpenSearchModuleImpl;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(
  cacheable = true,
  compressable = true,
  cacheKeys = { "locale" }
)
public class OpenSearchMapping extends FinalMapping
{

  /** Field description */
  private static final String MIMETYPE_DESCRIPTOR = "text/xml";

  /** Field description */
  private static final String MIMETYPE_SUGGESTION =
    "application/x-suggestions+json";

  /** Field description */
  private static final String PARAMETER_DESCRIPTOR = "descriptor.xml";

  /** Field description */
  private static final String PARAMETER_SEARCH = "search.xml";

  /** Field description */
  private static final String PARAMETER_SUGGESTION = "suggestion.json";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(OpenSearchMapping.class.getName());

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
    if ((param != null) && (param.length == 1))
    {
      if (PARAMETER_DESCRIPTOR.equals(param[0]))
      {
        printDescriptor(request, response);
      }
      else if (PARAMETER_SUGGESTION.equals(param[0]))
      {
        printSuggestion(request, response);
      }
      else if (PARAMETER_SEARCH.equals(param[0]))
      {
        printSearch(request, response);
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
   *
   * @throws IOException
   * @throws ServletException
   */
  protected void printDescriptor(BlogRequest request, BlogResponse response)
          throws IOException, ServletException
  {
    Blog blog = request.getCurrentBlog();
    String link = linkBuilder.buildLink(request, blog);

    response.setContentType(MIMETYPE_DESCRIPTOR);

    PrintWriter writer = null;

    try
    {
      writer = response.getWriter();
      writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      writer.print("<OpenSearchDescription ");
      writer.print("xmlns=\"http://a9.com/-/spec/opensearch/1.1/\" ");
      writer.print("xmlns:moz=\"http://www.mozilla.org/2006/browser/search/\"");
      writer.println(">");
      writer.println("\t<ShortName>JAB - " + blog.getTitle() + "</ShortName>");
      writer.println("\t<Description>" + blog.getDescription()
                     + "</Description>");

      // suggestion url
      writer.append("\t<Url type=\"").append(MIMETYPE_SUGGESTION);
      writer.append("\" template=\"").append(link).append("/opensearch/");
      writer.append(PARAMETER_SUGGESTION).append("?search={searchTerms}\" />");

      // atom search url
      writer.append("\t<Url type=\"application/atom+xml\" template=\"");
      writer.append(link).append("/opensearch/").append(PARAMETER_SEARCH);
      writer.append("?type=atom&search={searchTerms}\" />\n");

      // rss search url
      writer.append("\t<Url type=\"application/rss+xml\" template=\"");
      writer.append(link).append("/opensearch/").append(PARAMETER_SEARCH);
      writer.append("?type=rss&search={searchTerms}\" />\n");

      // html search url
      writer.append("\t<Url type=\"text/html\" template=\"").append(link);
      writer.append("/search.jab?search={searchTerms}\" />\n");
      writer.println("\t<Query role=\"example\" searchTerms=\"jab\" />");
      writer.println("\t<InputEncoding>UTF-8</InputEncoding>");
      writer.println("\t<Image width=\"16\" height=\"16\">\t\t");
      writer.println(linkBuilder.buildLink(request,
              resources.getJabIcon(DefaultWebResources.JABICON_16)));
      writer.println("\t</Image>");
      writer.println("\t<Image width=\"32\" height=\"32\">\t\t");
      writer.println(linkBuilder.buildLink(request,
              resources.getJabIcon(DefaultWebResources.JABICON_32)));
      writer.println("\t</Image>");
      writer.println("\t<Image width=\"64\" height=\"64\">\t\t");
      writer.println(linkBuilder.buildLink(request,
              resources.getJabIcon(DefaultWebResources.JABICON_64)));
      writer.println("\t</Image>");
      writer.println("\t<Image width=\"128\" height=\"128\">\t\t");
      writer.println(linkBuilder.buildLink(request,
              resources.getJabIcon(DefaultWebResources.JABICON_128)));
      writer.println("\t</Image>");
      writer.println("\t<moz:SearchForm>" + link + "</moz:SearchForm>");
      writer.println("</OpenSearchDescription>");
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBuilder
   * @param entries
   * @param category
   */
  private void addEntries(BlogRequest request, LinkBuilder linkBuilder,
                          List<SyndEntry> entries, SearchCategory category)
  {
    SyndCategory syndCategory = new SyndCategoryImpl();

    syndCategory.setName(category.getLabel());

    List<SyndCategory> categories = new ArrayList<SyndCategory>();

    categories.add(syndCategory);

    for (SearchEntry entry : category.getEntries())
    {
      SyndEntry syndEntry = new SyndEntryImpl();

      syndEntry.setCategories(categories);
      syndEntry.setAuthor(entry.getAuthorName());

      SyndContent content = new SyndContentImpl();

      content.setType("text/html");
      content.setValue(entry.getContent());
      syndEntry.setTitle(entry.getTitle());
      syndEntry.setPublishedDate(entry.getCreationDate());

      String entryLink = linkBuilder.buildLink(request, entry.getData());

      syndEntry.setLink(entryLink);
      entries.add(syndEntry);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param categories
   * @param type
   * @param query
   *
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  private void printOpenSearchFeed(BlogRequest request, BlogResponse response,
                                   List<SearchCategory> categories,
                                   String type, String query)
          throws IOException
  {
    if ("rss".equals(type))
    {
      type = "rss_2.0";
    }
    else if ("atom".equals(type))
    {
      type = "atom_1.0";
    }
    else
    {
      throw new IllegalArgumentException("unknown type " + type);
    }

    SyndFeed feed = new SyndFeedImpl();

    feed.setFeedType(type);

    List mods = feed.getModules();

    if (mods == null)
    {
      mods = new ArrayList();
    }

    OpenSearchModule osm = new OpenSearchModuleImpl();

    osm.setStartIndex(1);

    OSQuery osQuery = new OSQuery();

    osQuery.setRole("superset");
    osQuery.setSearchTerms(query);
    osQuery.setStartPage(1);
    osm.addQuery(osQuery);

    Blog blog = request.getCurrentBlog();
    String blogLink = linkBuilder.buildLink(request, blog);
    StringBuffer linkBuffer = new StringBuffer();

    linkBuffer.append(blogLink);
    linkBuffer.append("/opensearch/").append(PARAMETER_DESCRIPTOR);

    Link link = new Link();

    link.setHref(linkBuffer.toString());
    link.setType(PARAMETER_DESCRIPTOR);
    osm.setLink(link);
    mods.add(osm);
    feed.setModules(mods);
    feed.setTitle(blog.getTitle());
    feed.setDescription(blog.getDescription());
    feed.setPublishedDate(blog.getCreationDate());
    feed.setLink(blogLink);

    if (Util.isNotEmpty(categories))
    {
      List<SyndEntry> entries = new ArrayList<SyndEntry>();

      for (SearchCategory category : categories)
      {
        addEntries(request, linkBuilder, entries, category);
      }

      feed.setEntries(entries);
    }

    PrintWriter writer = null;

    try
    {
      writer = response.getWriter();

      SyndFeedOutput output = new SyndFeedOutput();

      output.output(feed, writer);
    }
    catch (FeedException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   * @throws ServletException
   */
  private void printSearch(BlogRequest request, BlogResponse response)
          throws IOException, ServletException
  {
    String type = request.getParameter("type");
    String query = request.getParameter("search");

    if (Util.isNotEmpty(type) && Util.isNotEmpty(query))
    {
      SearchContext searchCtx = BlogContext.getInstance().getSearchContext();

      if (searchCtx != null)
      {
        List<SearchCategory> categoryList =
          searchCtx.search(request.getCurrentBlog(), request.getLocale(),
                           query);

        printOpenSearchFeed(request, response, categoryList, type, query);
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
      }
      else
      {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
   *
   * @throws IOException
   * @throws ServletException
   */
  private void printSuggestion(BlogRequest request, BlogResponse response)
          throws IOException, ServletException
  {
    String query = request.getParameter("search");

    if (Util.isNotEmpty(query))
    {
      response.setContentType(MIMETYPE_SUGGESTION);

      PrintWriter writer = null;

      try
      {
        writer = response.getWriter();
        writer.append("[\"").append(query).append("\", [");

        TagDAO tagDAO = BlogContext.getDAOFactory().getTagDAO();
        List<String> tags = tagDAO.getTagNames(request.getCurrentBlog(), query,
                              0, 10);

        if (Util.isNotEmpty(tags))
        {
          Iterator<String> tagIt = tags.iterator();

          while (tagIt.hasNext())
          {
            String tag = tagIt.next();

            writer.append("\"").append(tag).append("\"");

            if (tagIt.hasNext())
            {
              writer.append(", ");
            }
          }
        }

        writer.append("]]");
      }
      finally
      {
        if (writer != null)
        {
          writer.close();
        }
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;

  /** Field description */
  @Context
  private DefaultWebResources resources;
}
