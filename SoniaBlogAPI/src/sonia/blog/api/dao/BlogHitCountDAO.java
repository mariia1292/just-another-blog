/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogHitCount;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author sdorra
 */
public interface BlogHitCountDAO extends GenericDAO<BlogHitCount>
{

  /**
   * Method description
   *
   *
   * @param blog
   * @param date
   *
   * @return
   */
  public BlogHitCount findByBlogAndDate(Blog blog, Date date);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public boolean increase(Blog blog);
}
