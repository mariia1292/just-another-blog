/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.BlogWrapper;
import sonia.blog.api.util.HitWrapper;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogHitCount;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;

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
   * @param month
   * @param year
   *
   * @return
   */
  public List<BlogWrapper> findByMonth(int month, int year);

  /**
   * Method description
   *
   *
   * @param year
   *
   * @return
   */
  public List<BlogWrapper> findByYear(int year);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public boolean increase(Blog blog);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param month
   * @param year
   *
   * @return
   */
  public Long getHitsByBlogAndMonth(Blog blog, int month, int year);

  /**
   * Method description
   *
   *
   * @param blog
   * @param year
   *
   * @return
   */
  public Long getHitsByBlogAndYear(Blog blog, int year);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<HitWrapper> getHitsPerMonthByBlog(Blog blog);
}
