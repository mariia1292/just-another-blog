/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
 */


package sonia.security.authentication;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.security.Principal;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class LoginModule implements javax.security.auth.spi.LoginModule
{

  /** Field description */
  private static Logger logger = Logger.getLogger(LoginModule.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param user
   *
   * @return
   */
  public abstract Collection<? extends Principal> getRoles(Principal user);

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
  protected abstract Principal handleLogin(String username, char[] password)
          throws LoginException;

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws LoginException
   */
  public boolean abort() throws LoginException
  {
    boolean result = true;

    if (user == null)
    {
      result = false;
    }
    else if (!commited)
    {
      user = null;
    }
    else
    {
      result = logout();
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws LoginException
   */
  public boolean commit() throws LoginException
  {
    boolean result = true;

    if (user == null)
    {
      result = false;
    }
    else
    {
      subject.getPrincipals().add(user);

      Collection<? extends Principal> roles = getRoles(user);

      if ((roles != null) && (roles.size() > 0))
      {
        subject.getPrincipals().addAll(roles);
      }

      this.commited = true;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param subject
   * @param callbackHandler
   * @param sharedState
   * @param options
   */
  public void initialize(Subject subject, CallbackHandler callbackHandler,
                         Map<String, ?> sharedState, Map<String, ?> options)
  {
    this.subject = subject;
    this.callbackHandler = callbackHandler;
    this.sharedState = sharedState;
    this.options = options;
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws LoginException
   */
  public boolean login() throws LoginException
  {
    boolean result = false;
    Callback[] callbacks = new Callback[2];

    callbacks[0] = new NameCallback("username");
    callbacks[1] = new PasswordCallback("password", false);

    try
    {
      callbackHandler.handle(callbacks);

      String username = ((NameCallback) callbacks[0]).getName();
      char[] password = ((PasswordCallback) callbacks[1]).getPassword();
      Principal u = handleLogin(username, password);

      if (u != null)
      {
        this.user = u;
        result = true;
      }
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    catch (UnsupportedCallbackException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws LoginException
   */
  public boolean logout() throws LoginException
  {
    subject.getPrincipals().clear();
    user = null;
    commited = false;

    return true;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Map<String, ?> options;

  /** Field description */
  protected Map<String, ?> sharedState;

  /** Field description */
  protected Subject subject;

  /** Field description */
  protected Principal user;

  /** Field description */
  private CallbackHandler callbackHandler;

  /** Field description */
  private boolean commited = false;
}