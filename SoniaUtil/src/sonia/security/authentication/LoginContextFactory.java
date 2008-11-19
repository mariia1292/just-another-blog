/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.authentication;

//~--- JDK imports ------------------------------------------------------------

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author sdorra
 */
public class LoginContextFactory
{

  /** Field description */
  private static LoginConfiguration configuration;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param username
   * @param password
   *
   * @return
   *
   * @throws LoginException
   */
  public static LoginContext buildLoginContext(String context, String username,
          char[] password)
          throws LoginException
  {
    return new LoginContext(context, new Subject(),
                            new LoginCallbackHandler(username, password),
                            configuration);
  }
}
