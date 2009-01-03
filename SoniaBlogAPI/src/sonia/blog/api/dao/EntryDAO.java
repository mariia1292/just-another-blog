/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;
import sonia.blog.entity.Category;
import sonia.blog.entity.Tag;

/**
 *
 * @author sdorra
 */
public interface EntryDAO extends GenericDAO<Entry>
{

  public List<Entry> findByCategory( Category category );

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
   * @param blog
   * @param startDate
   * @param endDate
   *
   * @return
   */
  public List<Date> findCalendarDates(Blog blog, Date startDate, Date endDate);

  public List<Entry> findByBlogAndDate( Blog blog, Date startDate, Date endDate );

  public List<Entry> findByBlogAndTag( Blog blog, Tag tag );
}
