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
   * @return
   */
  public List<User> findAllActive();

  public User findByNameAndCode( String name, String code );

  public User findByNameAndPassword( String name, String password );
}
