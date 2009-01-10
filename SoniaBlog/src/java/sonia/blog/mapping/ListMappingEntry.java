/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.faces.model.ListDataModel;

/**
 *
 * @author sdorra
 */
public class ListMappingEntry extends ScrollableMappingEntry
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
    Blog blog = request.getCurrentBlog();
    List<Entry> entries = buildList(blog);

    if (entries != null)
    {
      BlogBean blogBean = getBlogBean(request);

      end = (end < entries.size())
            ? end
            : entries.size();
      Collections.reverse(entries);

      List<Entry> pageEntries = entries.subList(start, end);

      blogBean.setEntries(entries);
      blogBean.setPageEntries(new ListDataModel(pageEntries));

      if ((param != null) && (param.length > 0))
      {
        if (!param[0].equals("index.jab"))
        {
          try
          {
            String part = param[0].substring(0, param[0].indexOf("."));
            long id = Long.parseLong(part);
            Entry entry = null;

            for (Entry e : entries)
            {
              if (e.getId() == id)
              {
                entry = e;
              }
            }

            if (entry != null)
            {
              blogBean.setEntry(entry);
              viewId = VIEW_DETAIL;
            }
            else
            {
              viewId = VIEW_NOTFOUND;
            }
          }
          catch (Exception ex)
          {
            logger.log(Level.WARNING, null, ex);
          }
        }
      }
    }
    else
    {
      viewId = VIEW_NOTFOUND;
    }

    setViewId(request, viewId);

    return true;
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  private List<Entry> buildList(Blog blog)
  {
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();

    return entryDAO.findAllActivesByBlog(blog);
  }
}
