/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.util.TagWrapper;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Tag;

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
public class JpaTagDAO extends JpaGenericDAO<Tag> implements TagDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaTagDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, Tag.class);
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
    return countQuery("Tag.count");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Tag> findAll()
  {
    return findList("Tag.findAll");
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Tag> findAllByBlog(Blog blog)
  {
    return findList("Tag.findAllByBlog", blog);
  }

  @SuppressWarnings("unchecked")
  public List<TagWrapper> findByBlogAndCount(Blog blog)
  {
    List<TagWrapper> tags = null;
    EntityManager em = createEntityManager();
    try
    {
      Query q = em.createNamedQuery("Tag.findByBlogAndCount");
      q.setParameter("blog", blog);
      tags = q.getResultList();
    }
    catch (NoResultException ex){}
    finally
    {
      if ( em != null )
      {
        em.close();
      }
    }
    return tags;
  }
}
