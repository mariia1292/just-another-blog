/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
        //TODO: use SystemBlogSession
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
