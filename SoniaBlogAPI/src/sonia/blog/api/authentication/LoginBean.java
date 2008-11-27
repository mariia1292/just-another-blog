/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.util.AbstractBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author sdorra
 */
public class LoginBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String login()
  {
    String result = FAILURE;

    try
    {
      if ((username == null) || (password == null))
      {
        throw new IllegalArgumentException("username or password is null");
      }

      loginContext = BlogContext.getInstance().buildLoginContext(username,
              password.toCharArray());
      loginContext.login();
      authenticated = true;
      result = SUCCESS;
      getMessageHandler().info("loginSuccess");
    }
    catch (LoginException ex)
    {
      getMessageHandler().warn("loginFailure");
      logger.log(Level.WARNING, null, ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String logout()
  {
    if (authenticated)
    {
      authenticated = false;
      password = null;

      try
      {
        loginContext.logout();
      }
      catch (LoginException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }

      loginContext = null;
    }

    getMessageHandler().info("logoutSuccess");

    return SUCCESS;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public LoginContext getLoginContext()
  {
    return loginContext;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAuthenticated()
  {
    return authenticated;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param password
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * Method description
   *
   *
   * @param username
   */
  public void setUsername(String username)
  {
    this.username = username;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean authenticated = false;

  /** Field description */
  private LoginContext loginContext;

  /** Field description */
  private String password;

  /** Field description */
  private String username;
}
