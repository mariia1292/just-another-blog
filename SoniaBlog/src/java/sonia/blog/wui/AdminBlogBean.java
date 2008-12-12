/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.util.AbstractBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class AdminBlogBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getBlogs()
  {
    blogs = new ListDataModel();

    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Blog.findAll");

    try
    {
      List list = q.getResultList();

      blogs.setWrappedData(list);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      em.close();
    }

    return blogs;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel blogs;
}
