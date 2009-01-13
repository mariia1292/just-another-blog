/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.entity.Blog;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class JpaBlogDAO extends JpaGenericDAO<Blog> implements BlogDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaBlogDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, Blog.class, Constants.LISTENER_BLOG);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public long count()
  {
    return countQuery("Blog.count");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Blog> findAll()
  {
    return findList("Blog.findAll");
  }

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> findAll(int start, int max)
  {
    return findList("Blog.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Blog> findAllActives()
  {
    return findList("Blog.findAllActives");
  }

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> findAllActives(int start, int max)
  {
    return findList("Blog.findAllActives", start, max);
  }

  /**
   * Method description
   *
   *
   * @param servername
   *
   * @return
   */
  public Blog findByServername(String servername)
  {
    Blog blog = null;
    EntityManager em = createEntityManager();

    try
    {
      Query q = em.createNamedQuery("Blog.findByServername");

      q.setParameter("servername", servername);
      blog = (Blog) q.getSingleResult();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return blog;
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  @Override
  public boolean remove(Blog blog)
  {
    boolean result = false;

    if (super.remove(blog))
    {
      ResourceManager resManager =
        BlogContext.getInstance().getResourceManager();
      File indexDir = resManager.getDirectory(Constants.RESOURCE_INDEX, blog,
                        false);

      if (indexDir.exists())
      {
        logger.fine("removing directory " + indexDir.getPath());
        Util.delete(indexDir);
      }

      File attachmentDir =
        resManager.getDirectory(Constants.RESOURCE_ATTACHMENT, blog, false);

      if (attachmentDir.exists())
      {
        logger.fine("removing directory " + attachmentDir.getPath());
        Util.delete(attachmentDir);
      }

      result = true;
    }

    return result;
  }
}
