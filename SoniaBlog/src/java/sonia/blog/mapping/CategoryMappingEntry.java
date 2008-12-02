/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.entity.Entry;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.faces.model.ListDataModel;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class CategoryMappingEntry extends ScrollableMappingEntry
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
    String view = VIEW_LIST;

    if ((param != null) && (param.length > 0))
    {
      try
      {
        long cid = Long.parseLong(param[0]);
        List<Entry> entries = buildList(cid);

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

          if ((param.length > 1) &&!param[1].equals("index.jab"))
          {
            String part = param[1].substring(0, param[1].indexOf("."));
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
              view = VIEW_DETAIL;
            }
            else
            {
              view = VIEW_NOTFOUND;
            }
          }
        }
        else
        {
          view = VIEW_NOTFOUND;
        }
      }
      catch (NumberFormatException ex)
      {
        view = VIEW_NOTFOUND;
      }
    }
    else
    {
      view = VIEW_NOTFOUND;
    }

    setViewId(request, view);

    return true;
  }

  /**
   * Method description
   *
   *
   * @param id
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private List<Entry> buildList(long id)
  {
    List<Entry> list = null;
    EntityManager em = BlogContext.getInstance().getEntityManager();

    try
    {
      Query q = em.createNamedQuery("Entry.findByCategoryId");

      q.setParameter("id", id);
      list = q.getResultList();
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return list;
  }
}
