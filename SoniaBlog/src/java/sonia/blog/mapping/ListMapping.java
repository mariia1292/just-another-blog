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
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.api.mapping.ScrollableFilterMapping;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;
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
 * @author sdorra
 */
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
      LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
      String nextUri = null;
      String prevUri = null;

      if (prev != null)
      {
        prevUri = "/list/" + prev.getId() + ".jab";
        prevUri = linkBuilder.buildLink(request, prevUri);
      }

      if (next != null)
      {
        nextUri = "/list/" + next.getId() + ".jab";
        nextUri = linkBuilder.buildLink(request, nextUri);
      }

      navigation = new SimpleMappingNavigation(prevUri, nextUri);
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
    String link = linkBuilder.buildLink(request, "/list/");

    navigation = new SimpleMappingNavigation(prevUri, nextUri,
            link + "{0,number,#}.jab");

    return buildTemplateViewId(blog, Constants.TEMPLATE_LIST);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private MappingNavigation navigation;
}
