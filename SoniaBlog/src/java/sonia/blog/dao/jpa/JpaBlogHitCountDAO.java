/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.BlogHitCountDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogHitCount;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import sonia.blog.api.app.Constants;

/**
 *
 * @author sdorra
 */
public class JpaBlogHitCountDAO extends JpaGenericDAO<BlogHitCount>
        implements BlogHitCountDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaBlogHitCountDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, BlogHitCount.class, Constants.LISTENER_BLOGHITCOUNT);
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
    return countQuery("BlogHitCount.count");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<BlogHitCount> findAll()
  {
    return findList("BlogHitCount.findAll");
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
  public List<BlogHitCount> findAll(int start, int max)
  {
    return findList("BlogHitCount.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param date
   *
   * @return
   */
  public BlogHitCount findByBlogAndDate(Blog blog, Date date)
  {
    BlogHitCount hitCount = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogHitCount.findByBlogAndDate");

    q.setParameter("blog", blog);
    q.setParameter("date", date);

    try
    {
      hitCount = (BlogHitCount) q.getSingleResult();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return hitCount;
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public boolean increase(Blog blog)
  {
    boolean result = true;
    Date date = new Date();
    BlogHitCount blogHitCount = findByBlogAndDate(blog, date);

    if (blogHitCount == null)
    {
      blogHitCount = new BlogHitCount(blog, date);
      blogHitCount.inc();
      add(blogHitCount);
    }
    else
    {
      blogHitCount.inc();
      edit(blogHitCount);
    }

    return result;
  }
}
