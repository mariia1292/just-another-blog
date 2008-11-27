/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;

import javax.persistence.EntityManager;

/**
 *
 * @author sdorra
 */
public class ConfigBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String reIndex()
  {
    BlogContext.getInstance().getSearchContext().reIndex(getBlog());

    return SUCCESS;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;
    EntityManager em = BlogContext.getInstance().getEntityManager();

    em.getTransaction().begin();

    try
    {
      blog = em.merge(blog);
      em.getTransaction().commit();
      getMessageHandler().info("unpdateConfigSuccess");
    }
    catch (Exception ex)
    {
      result = FAILURE;

      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      getMessageHandler().error("unpdateConfigFailure");
    }
    finally
    {
      em.close();
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    blog = getRequest().getCurrentBlog();

    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isSearchContextLocked()
  {
    return BlogContext.getInstance().getSearchContext().isLocked();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isSearchReIndexAble()
  {
    return BlogContext.getInstance().getSearchContext().isReIndexable();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;
}
