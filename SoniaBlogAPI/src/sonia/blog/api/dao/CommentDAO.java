/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface CommentDAO extends GenericDAO<Comment>
{

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public List<Comment> findAllActiveByEntry(Entry entry);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Comment> findAllByBlog(Blog blog);
}
