/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogHitCount;
import sonia.blog.entity.BlogMember;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface BlogDAO extends GenericDAO<Blog>
{

  /**
   * Method description
   *
   *
   * @param identifier
   * @param active
   *
   * @return
   */
  public Blog get(String identifier, boolean active);

  /**
   * Method description
   *
   *
   * @param identifier
   *
   * @return
   */
  public Blog get(String identifier);

  /**
   * Method description
   *
   *
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> getAll(boolean active, int start, int max);

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
  public List<BlogMember> getMembers(Blog blog, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<BlogMember> getMembers(Blog blog, boolean active, int start,
                                     int max);
}
