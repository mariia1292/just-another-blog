/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

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
   * @param name
   *
   * @return
   */
  public User findActiveByName(String name);

  /**
   * Method description
   *
   *
   * @return
   */
  public List<User> findAllActives();

  /**
   * Method description
   *
   *
   * @param email
   *
   * @return
   */
  public User findByEmail(String email);

  /**
   * Method description
   *
   *
   *
   * @param name
   *
   * @return
   */
  public User findByName(String name);

  /**
   * Method description
   *
   *
   * @param name
   * @param code
   *
   * @return
   */
  public User findByNameAndCode(String name, String code);

  /**
   * Method description
   *
   *
   * @param name
   * @param password
   *
   * @return
   */
  public User findByNameAndPassword(String name, String password);
}
