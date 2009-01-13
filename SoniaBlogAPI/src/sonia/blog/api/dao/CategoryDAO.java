/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface CategoryDAO extends GenericDAO<Category>
{

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long countByBlog(Blog blog);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Category> findAllByBlog(Blog blog);

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
  public List<Category> findAllByBlog(Blog blog, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public Category findFirstByBlog(Blog blog);
}
