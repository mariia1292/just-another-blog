/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.authentication;

//~--- JDK imports ------------------------------------------------------------

import java.security.Principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.security.auth.login.LoginException;

/**
 *
 * @author sdorra
 */
public class SampleLoginModule extends LoginModule
{

  /** Field description */
  public static final String PASSWORD = "schalter";

  /** Field description */
  public static final String ROLENAME = "samplerole";

  /** Field description */
  public static final String USERNAME = "hans";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public SampleLoginModule() {}

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param user
   *
   * @return
   */
  @Override
  public Collection<? extends Principal> getRoles(Principal user)
  {
    List<Principal> roles = new ArrayList<Principal>();

    roles.add(new Principal()
    {
      public String getName()
      {
        return ROLENAME;
      }
    });

    return roles;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param username
   * @param password
   *
   * @return
   *
   * @throws LoginException
   */
  @Override
  protected Principal handleLogin(final String username, char[] password)
          throws LoginException
  {
    Principal p = null;

    if (username.equals(USERNAME) && new String(password).equals(PASSWORD))
    {
      p = new Principal()
      {
        public String getName()
        {
          return username;
        }
      };
    }

    return p;
  }
}
