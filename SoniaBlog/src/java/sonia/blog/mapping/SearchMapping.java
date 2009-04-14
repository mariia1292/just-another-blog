/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.entity.Blog;
import sonia.blog.util.BlogUtil;
import sonia.blog.wui.BlogBean;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;
import java.util.logging.Level;

import javax.faces.model.ListDataModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author sdorra
 */
public class SearchMapping extends ScrollableFilterMapping
{

  /** Field description */
  private static final String SESSION_VAR = "sonia.session.search";

  /** Field description */
  private static final int TIMEOUT = 1000 * 60 * 5;

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
    String searchParam = request.getParameter("search");

    if (!Util.isBlank(searchParam))
    {
      Blog blog = request.getCurrentBlog();
      List<SearchEntry> entries = null;
      HttpSession session = request.getSession(true);
      SearchSession searchSession =
        (SearchSession) session.getAttribute(SESSION_VAR);

      if (searchSession != null)
      {
        long currentTime = System.currentTimeMillis();

        if (!searchSession.getBlog().equals(blog)
            || (currentTime - searchSession.getTime() > TIMEOUT))
        {
          session.removeAttribute(SESSION_VAR);
        }
        else
        {
          entries = searchSession.getEntries();
        }
      }

      if (entries == null)
      {
        SearchContext ctx = BlogContext.getInstance().getSearchContext();

        try
        {
          entries = ctx.search(blog, searchParam);
        }
        catch (SearchException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }

      String hitParam = request.getParameter("hit");

      if (!Util.isBlank(hitParam))
      {
        try
        {
          int hit = Integer.parseInt(hitParam);

          result = handleDetailView(request, searchParam, entries, hit);
        }
        catch (NumberFormatException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
      else
      {
        result = handleListView(request, searchParam, entries, start, max);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param searchParam
   * @param entries
   * @param hit
   *
   * @return
   */
  private String handleDetailView(BlogRequest request, String searchParam,
                                  List<SearchEntry> entries, int hit)
  {
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
      result = buildTemplateViewId(request, Constants.TEMPLATE_DETAIL);

      BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                            BlogBean.NAME);

      blogBean.setEntry(entry.getData());

      LinkBuilder builder = BlogContext.getInstance().getLinkBuilder();
      String prefix = builder.buildLink(request, "/search.jab");

      prefix += "?search=" + searchParam + "&hit=";

      String previousUri = null;
      String nextUri = null;

      if (hit > 0)
      {
        SearchEntry pe = entries.get(hit - 1);

        previousUri = prefix + pe.getId();
      }

      if ((hit + 1) < entries.size())
      {
        SearchEntry ne = entries.get(hit + 1);

        nextUri = prefix + ne.getId();
      }

      navigation = new SimpleMappingNavigation(previousUri, nextUri);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param searchParam
   * @param entries
   * @param start
   * @param end
   *
   * @return
   */
  private String handleListView(BlogRequest request, String searchParam,
                                List<SearchEntry> entries, int start, int end)
  {
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
    detailPattern.append("&hit={0,number,#}");

    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String pattern = linkBuilder.buildLink(request, detailPattern.toString());
    BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                          BlogBean.NAME);

    blogBean.setPageEntries(new ListDataModel(entries));
    navigation = new SimpleMappingNavigation(prevUri, nextUri, pattern);

    return buildTemplateViewId(request, Constants.TEMPLATE_LIST);
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/01/29
   * @author     Enter your name here...
   */
  private static class SearchSession
  {

    /**
     * Constructs ...
     *
     *
     * @param search
     * @param blog
     * @param entries
     */
    public SearchSession(String search, Blog blog, List<SearchEntry> entries)
    {
      this.search = search;
      this.blog = blog;
      this.entries = entries;
      this.time = 0l;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public Blog getBlog()
    {
      return blog;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public List<SearchEntry> getEntries()
    {
      return entries;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getSearch()
    {
      return search;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public long getTime()
    {
      return time;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private Blog blog;

    /** Field description */
    private List<SearchEntry> entries;

    /** Field description */
    private String search;

    /** Field description */
    private long time;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private MappingNavigation navigation;
}
