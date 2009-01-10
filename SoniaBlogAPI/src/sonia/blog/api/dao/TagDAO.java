/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.TagWrapper;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Tag;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface TagDAO extends GenericDAO<Tag>
{

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Tag> findAllByBlog(Blog blog);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<TagWrapper> findByBlogAndCount(Blog blog);

  /**
   * Method description
   *
   *
   *
   * @param name
   * @return
   */
  public Tag findByName(String name);
}
