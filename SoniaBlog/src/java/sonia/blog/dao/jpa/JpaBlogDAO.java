/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.BlogDAO;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

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
    super(entityManagerFactory, Blog.class);
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

  public List<Blog> findAllActives()
  {
    return findList("Blog.findAllActives");
  }
}
