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


package sonia.blog.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.authentication.RolePrincipal;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.service.ServiceReference;

import sonia.security.authentication.LoginModule;
import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.security.Principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultLoginModule extends LoginModule
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultLoginModule.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DefaultLoginModule()
  {
    reference =
      BlogContext.getInstance().getServiceRegistry().get(Encryption.class,
        Constants.SERVCIE_ENCRYPTION);
  }

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
    List<RolePrincipal> roles = new ArrayList<RolePrincipal>();
    User u = (User) user;

    if (u.isGlobalAdmin())
    {
      roles.add(new RolePrincipal(Role.ADMIN));
    }

    return null;
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
  protected Principal handleLogin(String username, char[] password)
          throws LoginException
  {
    logger.info(username + " try to login");

    String passwordString = new String(password);

    if ((reference != null) && (reference.get() != null))
    {
      Encryption enc = reference.get();

      passwordString = enc.encrypt(passwordString);
    }

    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();
    User u = userDAO.get(username, passwordString, true);

    if (u != null)
    {
      u.setLastLogin(new Date());
      userDAO.edit(u);
    }

    return u;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference<Encryption> reference;
}