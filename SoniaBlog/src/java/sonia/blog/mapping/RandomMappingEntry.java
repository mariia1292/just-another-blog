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
import sonia.blog.entity.Entry;
import sonia.blog.entity.PermaObject;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class RandomMappingEntry extends AbstractMappingEntry
{

  /**
   * Constructs ...
   *
   */
  public RandomMappingEntry()
  {
    super();
    random = new Random();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public boolean handleMapping(BlogRequest request, BlogResponse response,
                               String[] param)
  {
    String viewId = VIEW_DETAIL;
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Entry.findByBlog");

    q.setParameter("blog", request.getCurrentBlog());

    List<Entry> entries = q.getResultList();

    if ((entries != null) &&!entries.isEmpty())
    {
      BlogBean blogBean = getBlogBean(request);
      int size = entries.size();
      int index = random.nextInt(size);
      Entry e = entries.get(index);

      blogBean.setEntry(e);
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
  public String getUri(BlogRequest request, LinkBuilder linkBuilder,
                       PermaObject object)
  {
    return linkBuilder.buildLink(request, "random.jab");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public boolean isNavigationRendered()
  {
    return false;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Random random;
}
