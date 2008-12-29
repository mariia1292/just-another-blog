/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.User;

import sonia.config.XmlConfiguration;

import sonia.plugin.ServiceReference;

import sonia.security.cipher.Cipher;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import javax.servlet.http.Cookie;

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
   */
  private void createCookie()
  {
    BlogContext context = BlogContext.getInstance();
    EntityManager em = context.getEntityManager();

    try
    {
      Query q = em.createNamedQuery("User.findActiveByName");

      q.setParameter("name", username);

      User user = (User) q.getSingleResult();
      String value = user.getName() + ":" + user.getActivationCode();

      if (cipherReference == null)
      {
        cipherReference = context.getServiceRegistry().getServiceReference(
          Constants.SERVCIE_CIPHER);
      }

      Cipher cipher = (Cipher) cipherReference.getImplementation();

      if (cipher != null)
      {
        value = cipher.encode(Constants.SECRET_KEY, value);
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
    finally
    {
      em.close();
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference cipherReference;

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
