/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;

/**
 *
 * @author sdorra
 */
public interface EntryDAO extends GenericDAO<Entry>
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
   * @return
   */
  public List<Entry> findAllActives();

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Entry> findAllActivesByBlog(Blog blog);

  /**
   * Method description
   *
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Entry> findAllByBlog(Blog blog);

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   *
   * @return
   */
  public List<Entry> findAllByBlogAndDate(Blog blog, Date startDate,
          Date endDate);

  /**
   * Method description
   *
   *
   * @param blog
   * @param tag
   *
   * @return
   */
  public List<Entry> findAllByBlogAndTag(Blog blog, Tag tag);

  /**
   * Method description
   *
   *
   * @param category
   *
   * @return
   */
  public List<Entry> findAllByCategory(Category category);

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   *
   * @return
   */
  public List<Date> findAllCalendarDates(Blog blog, Date startDate,
          Date endDate);

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   *
   * @return
   */
  public List<Entry> findAllDraftsByBlogAndUser(Blog blog, User user);
}
