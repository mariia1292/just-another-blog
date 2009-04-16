/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.config.XmlConfiguration;

import sonia.plugin.service.ServiceReference;

import sonia.security.KeyGenerator;
import sonia.security.cipher.Cipher;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import javax.servlet.http.Cookie;

/**
 *
 * @author sdorra
 */
public class LoginBean extends AbstractBean
{

  /** Field description */
  private static Logger logger = Logger.getLogger(LoginBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public LoginBean()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

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
      checkMembership(getRequest().getCurrentBlog());
      result = SUCCESS;

      if (cookie)
      {
        createCookie();
      }

      getMessageHandler().info("loginSuccess");
      redirect();
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
   * @param request
   * @param response
   */
  public void login(BlogRequest request, BlogResponse response)
  {
    try
    {
      loginContext = BlogContext.getInstance().buildSSOLoginContext(request,
              response);
      loginContext.login();
      checkMembership(request.getCurrentBlog());
      authenticated = true;
    }
    catch (LoginException ex)
    {
      logger.log(Level.FINER, null, ex);
    }
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

    Cookie[] cookies = getRequest().getCookies();

    if ((cookies != null) && (cookies.length > 0))
    {
      for (Cookie c : cookies)
      {
        if (c.getName().equals(Constants.COOKIE_NAME))
        {
          c.setMaxAge(0);
          getResponse().addCookie(c);
        }
      }
    }

    getMessageHandler().info("logoutSuccess");
    redirect();

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

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isCookie()
  {
    return cookie;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isSsoEnabled()
  {
    if (configuration == null)
    {
      configuration = BlogContext.getInstance().getConfiguration();
    }

    return configuration.getInteger(
        Constants.CONFIG_SSO,
        Constants.SSO_ONEPERSESSION) != Constants.SSO_DISABLED;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cookie
   */
  public void setCookie(boolean cookie)
  {
    this.cookie = cookie;
  }

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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   */
  private void checkMembership(Blog blog)
  {
    Role role = null;
    Set<User> users = loginContext.getSubject().getPrincipals(User.class);

    if ((users != null) &&!users.isEmpty())
    {
      User user = users.iterator().next();
      UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

      role = userDAO.getRole(blog, user);

      if (role == null)
      {
        role = getDefaultRole();
        userDAO.setRole(blog, user, role);
      }
    }
  }

  /**
   * Method description
   *
   */
  private void createCookie()
  {
    BlogContext context = BlogContext.getInstance();

    try
    {
      User user = BlogContext.getDAOFactory().getUserDAO().get(username, true);
      String value = user.getName() + ":" + user.getActivationCode();

      if (cipherReference == null)
      {
        cipherReference = context.getServiceRegistry().get(Cipher.class,
                Constants.SERVCIE_CIPHER);
      }

      Cipher cipher = cipherReference.get();

      if (cipher != null)
      {
        char[] secretKey = getSecretKey();

        value = cipher.encode(secretKey, value);
      }

      Cookie c = new Cookie(Constants.COOKIE_NAME, value);

      c.setMaxAge(
          context.getConfiguration().getInteger(
            Constants.CONFIG_COKKIETIME, Constants.DEFAULT_COOKIETIME));
      getResponse().addCookie(c);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Method description
   *
   */
  private void redirect()
  {
    BlogRequest request = getRequest();
    String uri = BlogContext.getInstance().getLinkBuilder().buildLink(request,
                   request.getCurrentBlog());

    sendRedirect(uri);
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
    Role role = null;
    String value = BlogContext.getInstance().getConfiguration().getString(
                       Constants.CONFIG_DEFAULTROLE);

    if (Util.isBlank(value))
    {
      role = Role.READER;
    }
    else
    {
      role = Role.valueOf(value);
    }

    return role;
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws IOException
   */
  private char[] getSecretKey() throws IOException
  {
    BlogConfiguration config = BlogContext.getInstance().getConfiguration();
    String key = config.getString(Constants.CONFIG_COOKIEKEY);

    if (Util.isBlank(key))
    {
      key = KeyGenerator.generateKey(16);
      config.set(Constants.CONFIG_COOKIEKEY, key);
      config.store();
    }

    return key.toCharArray();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference<Cipher> cipherReference;

  /** Field description */
  private XmlConfiguration configuration;

  /** Field description */
  private boolean cookie = false;

  /** Field description */
  private boolean authenticated = false;

  /** Field description */
  private LoginContext loginContext;

  /** Field description */
  private String password;

  /** Field description */
  private String username;
}
