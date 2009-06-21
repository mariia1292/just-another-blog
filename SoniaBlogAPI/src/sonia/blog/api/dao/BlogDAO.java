/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogSession;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;

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
   * @param active
   * @param notify
   *
   * @return
   */
  public List<BlogMember> getMembers(Blog blog, boolean active, boolean notify);

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

  /**
   * Method description
   *
   *
   * @param blog
   * @param name
   *
   * @return
   */
  public String getParameter(Blog blog, String name);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public Map<String, String> getParameters(Blog blog);

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param session
   * @param blog
   * @param name
   * @param value
   */
  public void setParameter(BlogSession session, Blog blog, String name,
                           String value);
}
