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
import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.api.mapping.ScrollableFilterMapping;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;
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
public class TagMapping extends ScrollableFilterMapping
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
        Long tagId = Long.parseLong(param[0]);
        TagDAO tagDAO = BlogContext.getDAOFactory().getTagDAO();
        Tag tag = tagDAO.get(tagId);
        Blog blog = request.getCurrentBlog();

        if ((tag != null))
        {
          if (param.length > 1)
          {
            Long entryId = Long.parseLong(param[1]);

            result = handleDetailView(request, blog, tag, entryId);
          }
          else
          {
            result = handleListView(request, blog, tag, start, max);
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
   *
   * @param request
   * @param blog
   * @param tag
   * @param entryId
   *
   * @return
   */
  private String handleDetailView(BlogRequest request, Blog blog, Tag tag,
                                  Long entryId)
  {
    String result = null;
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    Entry entry = entryDAO.get(entryId);

    if (entry != null)
    {
      if (logger.isLoggable(Level.FINER))
      {
        logger.finer("set entry(" + entry.getId() + ") to BlogBean");
      }

      BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                            BlogBean.NAME);

      blogBean.setEntry(entry);

      Entry next = entryDAO.getNextEntry(blog, tag, entry, true);
      Entry prev = entryDAO.getPreviousEntry(blog, tag, entry, true);
      String prefix = "/tag/" + tag.getId() + "/";
      LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
      String nextUri = null;
      String prevUri = null;

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
   * @param blog
   * @param tag
   * @param start
   * @param end
   *
   * @return
   */
  private String handleListView(BlogRequest request, Blog blog, Tag tag,
                                int start, int end)
  {
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    List<Entry> entries = entryDAO.findAllByBlogAndTag(blog, tag, start,
                            end + 1);
    String prevUri = null;
    String nextUri = null;

    if (logger.isLoggable(Level.FINER))
    {
      logger.finer("set entry list(" + entries.size() + ") to BlogBean");
    }

    BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                          BlogBean.NAME);

    blogBean.setPageEntries(new ListDataModel(entries));

    if (start > 0)
    {
      int page = getCurrentPage(request);

      if (page > 0)
      {
        prevUri = getPageUri(request, page - 1);
      }
    }

    if ((entries != null) && (entries.size() > end - start))
    {
      int page = getCurrentPage(request) + 1;
      int entriesPerPage = blog.getEntriesPerPage();

      if (entries.size() > (page * entriesPerPage))
      {
        nextUri = getPageUri(request, page);
      }
    }

    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String link = linkBuilder.buildLink(request, "/tag/" + tag.getId() + "/");

    navigation = new SimpleMappingNavigation(prevUri, nextUri,
            link + "{0}.jab");

    return buildTemplateViewId(blog, Constants.TEMPLATE_LIST);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private MappingNavigation navigation;
}
