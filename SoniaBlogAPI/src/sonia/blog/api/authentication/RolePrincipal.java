/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Role;

//~--- JDK imports ------------------------------------------------------------

import java.security.Principal;

/**
 *
 * @author sdorra
 */
public class RolePrincipal implements Principal
{

  /**
   * Constructs ...
   *
   *
   * @param role
   */
  public RolePrincipal(Role role)
  {
    this.role = role;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return role.name();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Role role;
}
