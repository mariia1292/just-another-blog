/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;

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
public class JpaCommentDAO extends JpaGenericDAO<Comment> implements CommentDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaCommentDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, Comment.class, Constants.LISTENER_COMMENT);
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
    return countQuery("Comment.count");
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
    return countQuery("Comment.countByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Comment> findAll()
  {
    return findList("Comment.findAll");
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
  public List<Comment> findAll(int start, int max)
  {
    return findList("Comment.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public List<Comment> findAllActivesByEntry(Entry entry)
  {
    return findAllActivesByEntry(entry, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @param entry
   * @param start
   * @param max
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Comment> findAllActivesByEntry(Entry entry, int start, int max)
  {
    List<Comment> comments = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Comment.findAllActivesByEntry");

    q.setParameter("entry", entry);

    if (start > 0)
    {
      q.setFirstResult(start);
    }

    if (max > 0)
    {
      q.setMaxResults(max);
    }

    try
    {
      comments = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return comments;
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Comment> findAllByBlog(Blog blog)
  {
    return findList("Comment.findAllByBlog", blog);
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
  public List<Comment> findAllByBlog(Blog blog, int start, int max)
  {
    return findList("Comment.findAllByBlog", blog, start, max);
  }
}
