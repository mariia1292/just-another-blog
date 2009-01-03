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

import javax.persistence.EntityManagerFactory;

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
  public List<Comment> findAllActiveByEntry(Entry entry)
  {
    throw new UnsupportedOperationException("Not supported yet.");
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
