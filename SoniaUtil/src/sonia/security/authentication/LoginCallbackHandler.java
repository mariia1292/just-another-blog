/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.authentication;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *
 * @author sdorra
 */
public class LoginCallbackHandler implements CallbackHandler
{

  /**
   * Constructs ...
   *
   *
   * @param username
   * @param password
   */
  public LoginCallbackHandler(String username, char[] password)
  {
    this.username = username;
    this.password = password;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param callbacks
   *
   * @throws IOException
   * @throws UnsupportedCallbackException
   */
  public void handle(Callback[] callbacks)
          throws IOException, UnsupportedCallbackException
  {
    for (Callback callback : callbacks)
    {
      if (callback instanceof NameCallback)
      {
        ((NameCallback) callback).setName(username);
      }
      else if (callback instanceof PasswordCallback)
      {
        ((PasswordCallback) callback).setPassword(password);
      }
      else
      {
        throw new UnsupportedCallbackException(callback);
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private char[] password;

  /** Field description */
  private String username;
}
