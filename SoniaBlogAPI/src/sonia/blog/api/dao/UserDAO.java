/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface UserDAO extends GenericDAO<User>
{

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long count(Blog blog);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param username
   * @param active
   *
   * @return
   */
  public User get(String username, boolean active);

  /**
   * Method description
   *
   *
   * @param username
   *
   * @return
   */
  public User get(String username);

  /**
   * Method description
   *
   *
   * @param username
   * @param password
   * @param active
   *
   * @return
   */
  public User get(String username, String password, boolean active);

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
  public List<User> getAll(boolean active, int start, int max);

  /**
   * Method description
   *
   *
   * @param mail
   *
   * @return
   */
  public User getByMail(String mail);

  /**
   * Method description
   *
   *
   * @param username
   * @param code
   *
   * @return
   */
  public User getByNameAndCode(String username, String code);

  /**
   * Method description
   *
   *
   * @param user
   * @param start
   * @param max
   *
   * @return
   */
  public List<BlogMember> getMembers(User user, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   *
   * @return
   */
  public Role getRole(Blog blog, User user);

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   * @param role
   */
  public void setRole(Blog blog, User user, Role role);
}
