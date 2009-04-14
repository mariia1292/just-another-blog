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
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.api.mapping.ScrollableFilterMapping;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.util.BlogUtil;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;
import java.util.logging.Level;

import javax.faces.model.ListDataModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class CategoryMapping extends ScrollableFilterMapping
{

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
        Long categoryId = Long.parseLong(param[0]);
        CategoryDAO categoryDAO = BlogContext.getDAOFactory().getCategoryDAO();
        Category category = categoryDAO.get(categoryId);
        Blog blog = request.getCurrentBlog();

        if ((category != null) && (category.getBlog() != null)
            && category.getBlog().equals(blog))
        {
          if (param.length > 1)
          {
            Long entryId = Long.parseLong(param[1]);

            result = handleDetailView(request, category, entryId);
          }
          else
          {
            result = handleListView(request, category, start, max);
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
   * @param category
   * @param entryId
   *
   * @return
   */
  private String handleDetailView(BlogRequest request, Category category,
                                  Long entryId)
  {
    String result = null;
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    Entry entry = entryDAO.get(entryId);

    if ((entry != null) && entry.containsCategory(category))
    {
      if (logger.isLoggable(Level.FINER))
      {
        logger.finer("set entry(" + entry.getId() + ") to BlogBean");
      }

      BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                            BlogBean.NAME);

      blogBean.setEntry(entry);

      Entry prev = entryDAO.getPreviousEntry(category, entry, true);
      Entry next = entryDAO.getNextEntry(category, entry, true);
      String prefix = "/category/" + category.getId() + "/";
      LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
      String prevUri = null;
      String nextUri = null;

      if (prev != null)
      {
        prevUri = prefix + prev.getId() + ".jab";
        prevUri = linkBuilder.buildLink(request, prevUri);
      }

      if (next != null)
      {
        nextUri = prefix + next.getId() + ".jab";
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
   * @param category
   * @param start
   * @param max
   *
   * @return
   */
  private String handleListView(BlogRequest request, Category category,
                                int start, int max)
  {
    Blog blog = request.getCurrentBlog();
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    List<Entry> entries = entryDAO.findAllByCategory(category, start, max + 1);
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

    blogBean.setPageEntries(new ListDataModel(entries));

    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String link = linkBuilder.buildLink(request,
                    "/category/" + category.getId() + "/");

    navigation = new SimpleMappingNavigation(prevUri, nextUri,
            link + "{0,number,#}.jab");

    return buildTemplateViewId(blog, Constants.TEMPLATE_LIST);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private MappingNavigation navigation;
}
