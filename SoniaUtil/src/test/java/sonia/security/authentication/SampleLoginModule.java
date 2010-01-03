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

import java.security.Principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.security.auth.login.LoginException;

/**
 *
 * @author Sebastian Sdorra
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
