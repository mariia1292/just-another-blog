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
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.api.mapping.ScrollableFilterMapping;
import sonia.blog.api.search.SearchCategory;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;
import sonia.blog.util.BlogUtil;
import sonia.blog.wui.BlogBean;
import sonia.blog.wui.PageBean;
import sonia.blog.wui.SearchBean;

import sonia.cache.Cacheable;
import sonia.cache.ObjectCache;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

/**
 *
 * @author Sebastian Sdorra
 */
@Cacheable({ "user", "locale" })
public class SearchMapping extends ScrollableFilterMapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(SearchMapping.class.getName());

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
  @SuppressWarnings("unchecked")
  protected String handleScrollableFilterMapping(BlogRequest request,
          BlogResponse response, String[] param, int start, int max)
          throws IOException, ServletException
  {
    String result = null;
    String searchParam = request.getParameter("search");

    if (!Util.isBlank(searchParam))
    {
      Blog blog = request.getCurrentBlog();
      Locale locale = request.getLocale();
      ObjectCache cache =
        BlogContext.getInstance().getCacheManager().get(Constants.CACHE_SEARCH);
      CacheKey key = new CacheKey(blog, searchParam);
      List<SearchCategory> categories = (List<SearchCategory>) cache.get(key);

      if (Util.isEmpty(categories))
      {
        SearchContext ctx = BlogContext.getInstance().getSearchContext();

        try
        {
          categories = ctx.search(blog, locale, searchParam);

          if (Util.hasContent(categories))
          {
            cache.put(key, categories);
          }
        }
        catch (SearchException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }

      String hitParam = request.getParameter("hit");
      String categoryParam = request.getParameter("category");
      SearchCategory category = getCategory(categories, categoryParam);

      if (category != null)
      {
        if (Util.hasContent(hitParam))
        {
          try
          {
            int hit = Integer.parseInt(hitParam);

            result = handleDetailView(request, searchParam, categories,
                                      category, hit);
          }
          catch (NumberFormatException ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }
        else
        {
          result = handleListView(request, searchParam, categories, category,
                                  start, max);
        }
      }
    }
    else
    {

      // TODO: error handling
    }

    if (result == null)
    {
      result = buildTemplateViewId(request, Constants.TEMPLATE_SEARCH);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param searchParam
   * @param categories
   * @param category
   * @param hit
   *
   * @return
   */
  private String handleDetailView(BlogRequest request, String searchParam,
                                  List<SearchCategory> categories,
                                  SearchCategory category, int hit)
  {
    List<SearchEntry> entries = category.getEntries();
    String result = null;
    int index = -1;
    SearchEntry entry = null;

    for (int i = 0; i < entries.size(); i++)
    {
      SearchEntry e = entries.get(i);

      if (e.getId() == hit)
      {
        index = i;
        entry = e;
      }
    }

    if (index >= 0)
    {
      ContentObject co = entry.getData();

      setDisplayContent(request, co, false);

      if (co instanceof Entry)
      {
        BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                              BlogBean.NAME);

        blogBean.setEntry(co);
        result = buildTemplateViewId(request, Constants.TEMPLATE_DETAIL);
      }
      else if (co instanceof Page)
      {
        PageBean pageBean = new PageBean();

        pageBean.setPage((Page) co);
        request.setAttribute(PageBean.NAME, pageBean);
        result = buildTemplateViewId(request, Constants.TEMPLATE_PAGE);
      }

      StringBuilder prefix = new StringBuilder("/search.jab");

      prefix.append("?category=").append(category.getName());
      prefix.append("&search=").append(searchParam).append("&hit=");

      String previousUri = null;
      String nextUri = null;

      if (hit > 0)
      {
        SearchEntry pe = entries.get(hit - 1);

        previousUri = new StringBuilder(prefix).append(pe.getId()).toString();
      }

      if ((hit + 1) < entries.size())
      {
        SearchEntry ne = entries.get(hit + 1);

        nextUri = new StringBuilder(prefix).append(ne.getId()).toString();
      }

      String detailPattern =
        new StringBuilder(prefix).append("{0,number,#}").toString();

      navigation = new SimpleMappingNavigation(previousUri, nextUri,
              detailPattern);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param searchParam
   * @param categories
   * @param category
   * @param start
   * @param end
   *
   * @return
   */
  private String handleListView(BlogRequest request, String searchParam,
                                List<SearchCategory> categories,
                                SearchCategory category, int start, int end)
  {
    List<SearchEntry> entries = category.getEntries();
    Blog blog = request.getCurrentBlog();
    String prevUri = null;
    String nextUri = null;

    if (logger.isLoggable(Level.FINER))
    {
      logger.finer("set entry list(" + entries.size() + ") to BlogBean");
    }

    if (start > 0)
    {
      int page = getCurrentPage(request);

      if (page > 0)
      {
        prevUri = getPageUri(request, page - 1);
      }
    }

    int size = entries.size();

    if ((entries != null) && (size > end - start))
    {
      int page = getCurrentPage(request) + 1;
      int entriesPerPage = blog.getEntriesPerPage();

      if (size > (page * entriesPerPage))
      {
        nextUri = getPageUri(request, page);
      }

      if (size < end)
      {
        end = size;
      }

      entries = entries.subList(start, end);
    }

    StringBuffer detailPattern = new StringBuffer();

    detailPattern.append("/search.jab?search=").append(searchParam);
    detailPattern.append("&category=").append(category.getName());
    detailPattern.append("&hit={0,number,#}");

    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String pattern = linkBuilder.buildLink(request, detailPattern.toString());
    SearchBean searchBean = BlogUtil.getRequestBean(request, SearchBean.class,
                              SearchBean.NAME);

    searchBean.setSearchString(searchParam);
    searchBean.setCategory(category);
    searchBean.setCategories(categories);
    searchBean.setPageEntries(entries);
    navigation = new SimpleMappingNavigation(prevUri, nextUri, pattern);

    return buildTemplateViewId(request, Constants.TEMPLATE_SEARCH);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param categories
   * @param current
   *
   * @return
   */
  private SearchCategory getCategory(List<SearchCategory> categories,
                                     String current)
  {
    SearchCategory result = null;

    if (Util.hasContent(categories))
    {
      if (Util.hasContent(current))
      {
        for (SearchCategory category : categories)
        {
          if (category.getName().equals(current))
          {
            result = category;

            break;
          }
        }
      }

      if (result == null)
      {
        result = categories.get(0);
      }
    }

    return result;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/08/02
   * @author         Enter your name here...
   */
  private static class CacheKey
  {

    /**
     * Constructs ...
     *
     *
     * @param blog
     * @param query
     */
    public CacheKey(Blog blog, String query)
    {
      this.id = blog.getId();
      this.query = query;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param obj
     *
     * @return
     */
    @Override
    public boolean equals(Object obj)
    {
      if (obj == null)
      {
        return false;
      }

      if (getClass() != obj.getClass())
      {
        return false;
      }

      final CacheKey other = (CacheKey) obj;

      if ((this.id != other.id)
          && ((this.id == null) ||!this.id.equals(other.id)))
      {
        return false;
      }

      if ((this.query == null)
          ? (other.query != null)
          : !this.query.equals(other.query))
      {
        return false;
      }

      return true;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public int hashCode()
    {
      int hash = 3;

      hash = 29 * hash + ((this.id != null)
                          ? this.id.hashCode()
                          : 0);
      hash = 29 * hash + ((this.query != null)
                          ? this.query.hashCode()
                          : 0);

      return hash;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public Long getId()
    {
      return id;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getQuery()
    {
      return query;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private Long id;

    /** Field description */
    private String query;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private MappingNavigation navigation;
}
