/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.MemberDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.User;

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
public class JpaMemberDAO extends JpaGenericDAO<BlogMember> implements MemberDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaMemberDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, BlogMember.class, Constants.LISTENER_MEMBER);
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
    return countQuery("BlogMember.count");
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long countByBlog(Blog blog)
  {
    return countQuery("BlogMember.countByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<BlogMember> findAll()
  {
    return findList("BlogMember.findAll");
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
  public List<BlogMember> findAll(int start, int max)
  {
    return findList("BlogMember.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<BlogMember> findByBlog(Blog blog)
  {
    return findList("BlogMember.findByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   *
   * @return
   */
  public BlogMember findByBlogAndUser(Blog blog, User user)
  {
    BlogMember member = null;
    EntityManager em = createEntityManager();

    try
    {
      Query q = em.createNamedQuery("BlogMember.findByBlogAndUser");

      q.setParameter("blog", blog);
      q.setParameter("user", user);
      member = (BlogMember) q.getSingleResult();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return member;
  }

  /**
   * Method description
   *
   *
   * @param user
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<BlogMember> findByUser(User user)
  {
    List<BlogMember> members = null;
    EntityManager em = createEntityManager();

    try
    {
      Query q = em.createNamedQuery("BlogMember.findByUser");

      q.setParameter("user", user);
      members = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return members;
  }
}
