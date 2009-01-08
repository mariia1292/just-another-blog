/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface MemberDAO extends GenericDAO<BlogMember>
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
  public List<BlogMember> findByBlog(Blog blog);

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   *
   * @return
   */
  public List<BlogMember> findByBlogAndUser(Blog blog, User user);

  /**
   * Method description
   *
   *
   * @param user
   *
   * @return
   */
  public List<BlogMember> findByUser(User user);
}
