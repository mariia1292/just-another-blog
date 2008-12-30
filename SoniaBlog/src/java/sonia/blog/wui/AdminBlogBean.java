/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Category;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

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
  public String remove()
  {
    String result = SUCCESS;
    BlogContext context = BlogContext.getInstance();
    ResourceManager resManager = context.getResourceManager();
    Blog blog = (Blog) blogs.getRowData();

    if (blog != null)
    {
      if (!blog.equals(getRequest().getCurrentBlog()))
      {
        EntityManager em = context.getEntityManager();

        em.getTransaction().begin();

        try
        {
          List<Category> categories = blog.getCategories();

          for (Category c : categories)
          {
            em.remove(em.merge(c));
          }

          List<BlogMember> members = blog.getMembers();

          for (BlogMember m : members)
          {
            em.remove(em.merge(m));
          }

          em.remove(em.merge(blog));
          em.getTransaction().commit();

          File searchDir = resManager.getDirectory(Constants.RESOURCE_INDEX,
                             blog, false);

          if (searchDir.exists())
          {
            Util.delete(searchDir);
          }

          File attachmentDir =
            resManager.getDirectory(Constants.RESOURCE_ATTACHMENT, blog, false);

          if (attachmentDir.exists())
          {
            Util.delete(attachmentDir);
          }

          getMessageHandler().info(null, "successBlogDelete", null,
                                   blog.getTitle());
        }
        catch (Exception ex)
        {
          if (em.getTransaction().isActive())
          {
            em.getTransaction().rollback();
          }

          logger.log(Level.SEVERE, null, ex);
          getMessageHandler().error(null, "failureBlogDelete", null,
                                    blog.getTitle());
        }
        finally
        {
          em.close();
        }
      }
      else
      {
        getMessageHandler().warn("cantDeleteCurrentBlog");
        result = FAILURE;
      }
    }
    else
    {
      getMessageHandler().error("unknownError");
      result = FAILURE;
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
