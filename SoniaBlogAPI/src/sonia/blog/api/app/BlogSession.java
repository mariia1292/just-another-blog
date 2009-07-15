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


package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.UserDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import javax.security.auth.login.LoginContext;

/**
 *
 * @author Sebastian Sdorra
 */
public final class BlogSession implements Serializable
{

  /** Field description */
  public static final String SESSIONVAR = "sonia.blog.BlogSession";

  /** Field description */
  private static final long serialVersionUID = 9038045523812688801L;

  /** Field description */
  private static BlogSession systemBlogSession = new BlogSession();

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs System BlogSession
   * only for internal use
   *
   */
  private BlogSession()
  {
    role = Role.SYSTEM;
  }

  /**
   * Constructs ...
   *
   *
   * @param loginContext
   * @param user
   * @param blog
   */
  public BlogSession(LoginContext loginContext, User user, Blog blog)
  {
    this.loginContext = loginContext;
    this.user = user;
    this.blog = blog;
    init();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    return blog;
  }

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
  public User getUser()
  {
    return user;
  }

  /**
   * Method description
   *
   *
   * @param role
   *
   * @return
   */
  public boolean hasRole(Role role)
  {
    return (role != null) && (role.getValue() <= this.role.getValue());
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param role
   *
   * @return
   */
  public boolean hasRole(Blog blog, Role role)
  {
    boolean result = true;

    if (this.role != Role.GLOBALADMIN)
    {
      if (this.blog.equals(blog))
      {
        result = hasRole(role);
      }
      else
      {
        Role r = BlogContext.getDAOFactory().getUserDAO().getRole(blog, user);

        result = role.getValue() <= r.getValue();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param role
   *
   * @return
   */
  public boolean hasRole(Blog blog, String role)
  {
    return hasRole(blog, Role.valueOf(role));
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public boolean hasRole(String name)
  {
    return hasRole(Role.valueOf(name));
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void init()
  {
    if (user.isGlobalAdmin())
    {
      role = Role.GLOBALADMIN;
    }
    else
    {
      UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

      role = userDAO.getRole(blog, user);

      if (role == null)
      {
        role = getDefaultRole();

        // TODO: use SystemBlogSession
        userDAO.setRole(blog, user, role);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private Role getDefaultRole()
  {
    Role defaultRole = null;
    String value = BlogContext.getInstance().getConfiguration().getString(
                       Constants.CONFIG_DEFAULTROLE);

    if (Util.isBlank(value))
    {
      defaultRole = Role.READER;
    }
    else
    {
      defaultRole = Role.valueOf(value);
    }

    return defaultRole;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private LoginContext loginContext;

  /** Field description */
  private Role role;

  /** Field description */
  private User user;
}