/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

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
    super(entityManagerFactory, Comment.class);
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
   * @param entry
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Comment> findAllActivesByEntry(Entry entry)
  {
    List<Comment> comments = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Comment.findAllActivesByEntry");

    q.setParameter("entry", entry);

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
}
