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
import sonia.blog.entity.BlogMember;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class JpaBlogDAO extends JpaGenericDAO<Blog> implements BlogDAO
{

  /** Field description */
  private static Logger logger = Logger.getLogger(JpaBlogDAO.class.getName());

  //~--- constructors ---------------------------------------------------------

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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param identifier
   * @param active
   *
   * @return
   */
  public Blog get(String identifier, boolean active)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Blog.getByIdentifierAndActive");

    q.setParameter("identifier", identifier);
    q.setParameter("active", active);

    return excecuteQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param identifier
   *
   * @return
   */
  public Blog get(String identifier)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Blog.getByIdentifier");

    q.setParameter("identifier", identifier);

    return excecuteQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Blog> getAll()
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
  public List<Blog> getAll(int start, int max)
  {
    return findList("Blog.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> getAll(boolean active, int start, int max)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Blog.getByActive");

    q.setParameter("active", active);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param start
   * @param max
   *
   * @return
   */
  public List<BlogMember> getMembers(Blog blog, int start, int max)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogMember.getAllByBlog");

    q.setParameter("blog", blog);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(BlogMember.class, em, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<BlogMember> getMembers(Blog blog, boolean active, int start,
                                     int max)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogMember.getAllByBalogAndActive");

    q.setParameter("blog", blog);
    q.setParameter("active", active);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(BlogMember.class, em, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   *
   * @return
   */
  public List<BlogMember> getMembers(Blog blog, boolean active, boolean notify)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogMember.getAllByBlogActiveAndNotify");

    q.setParameter("blog", blog);
    q.setParameter("active", active);
    q.setParameter("notify", notify);

    return excecuteListQuery(BlogMember.class, em, q);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Logger getLogger()
  {
    return logger;
  }
}
