/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.PermaObject;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.faces.model.ListDataModel;

/**
 *
 * @author sdorra
 */
public class SearchMappingEntry extends ScrollableMappingEntry
{

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
   */
  @Override
  public boolean handleMapping(BlogRequest request, BlogResponse response,
                               String[] param, int start, int end)
  {
    String viewId = VIEW_LIST;
    String searchString = request.getParameter("search");

    if (searchString != null)
    {
      try
      {
        SearchContext sc = BlogContext.getInstance().getSearchContext();
        List<SearchEntry> entries = sc.search(request.getCurrentBlog(),
                                      searchString);

        if (entries != null)
        {
          BlogBean blogBean = getBlogBean(request);

          end = (end < entries.size())
                ? end
                : entries.size();

          List<SearchEntry> pageEntries = entries.subList(start, end);

          blogBean.setPageEntries(new ListDataModel(pageEntries));
          blogBean.setEntries(buildEntryList(entries));

          String hitString = request.getParameter("hit");

          if (hitString != null)
          {
            int hit = Integer.parseInt(hitString);
            SearchEntry entry = entries.get(hit);

            if ((entry != null) && (entry.getData() != null))
            {
              viewId = VIEW_DETAIL;
              blogBean.setEntry(entry.getData());
            }
            else
            {
              viewId = VIEW_NOTFOUND;
            }
          }
        }
      }
      catch (SearchException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
      catch (NumberFormatException ex)
      {
        logger.log(Level.FINE, null, ex);
      }
    }
    else
    {
      viewId = VIEW_NOTFOUND;
    }

    setViewId(request, viewId);

    return true;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBuilder
   * @param object
   *
   * @return
   */
  @Override
  public String getUri(BlogRequest request, LinkBuilder linkBuilder,
                       PermaObject object)
  {
    String uri = linkBuilder.buildLink(request, "/search.jab");

    uri += "?search=" + request.getParameter("search");

    int index = getBlogBean(request).getEntries().indexOf(object);

    uri += "&hit=" + index;

    return uri;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param entries
   *
   * @return
   */
  private List<ContentObject> buildEntryList(List<SearchEntry> entries)
  {
    List<ContentObject> objects = new ArrayList<ContentObject>();

    for (SearchEntry e : entries)
    {
      objects.add(e.getData());
    }

    return objects;
  }
}
