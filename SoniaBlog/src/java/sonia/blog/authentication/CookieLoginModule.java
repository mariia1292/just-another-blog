/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.authentication;

//~--- non-JDK imports --------------------------------------------------------

import java.io.IOException;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.authentication.SSOLoginModule;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.entity.User;

import sonia.plugin.service.ServiceReference;

import sonia.security.cipher.Cipher;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

import javax.security.auth.login.LoginException;

import javax.servlet.http.Cookie;
import sonia.blog.api.app.BlogConfiguration;
import sonia.security.KeyGenerator;

/**
 *
 * @author sdorra
 */
public class CookieLoginModule extends SSOLoginModule
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @return
   *
   * @throws LoginException
   */
  @Override
  public User handleLogin(BlogRequest request, BlogResponse response)
          throws LoginException
  {
    User user = null;
    Cipher cipher = null;

    if (cipherReference == null)
    {
      cipherReference =
        BlogContext.getInstance().getServiceRegistry().get(Cipher.class,
          Constants.SERVCIE_CIPHER);
    }

    cipher = cipherReference.get();

    Cookie[] cookies = request.getCookies();

    if ((cookies != null) && (cookies.length > 0))
    {
      for (Cookie cookie : cookies)
      {
        if (cookie.getName().equals(Constants.COOKIE_NAME))
        {
          String value = cookie.getValue();

          if (cipher != null)
          {
            char[] secretKey = getSecretKey();
            if ( secretKey == null )
            {
              logger.severe( "CookieKey is null" );
              throw new IllegalStateException("CookieKey is null");
            }

            value = cipher.decode(secretKey, value);
          }

          user = checkCookieValue(response, cookie, value);

          break;
        }
      }
    }

    return user;
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param cookie
   * @param value
   *
   * @return
   */
  private User checkCookieValue(BlogResponse response, Cookie cookie,
                                String value)
  {
    User user = null;

    if (!Util.isBlank(value))
    {
      String[] parts = value.split(":");

      if ((parts != null) && (parts.length == 2))
      {
        String username = parts[0];
        String activationCode = parts[1];

        user = login(response, cookie, username, activationCode);
      }
    }

    return user;
  }

  private char[] getSecretKey()
  {
    char[] secretKey = null;
    BlogConfiguration config = BlogContext.getInstance().getConfiguration();
    String key = config.getString(Constants.CONFIG_COOKIEKEY);
    if ( key != null )
    {
      secretKey = key.toCharArray();
    }
    return secretKey;
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param cookie
   * @param username
   * @param activationCode
   *
   * @return
   */
  private User login(BlogResponse response, Cookie cookie, String username,
                     String activationCode)
  {
    BlogContext context = BlogContext.getInstance();
    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();
    User user = userDAO.findByNameAndCode(username, activationCode);

    if (user != null)
    {
      cookie.setMaxAge(
          context.getConfiguration().getInteger(
            Constants.CONFIG_COKKIETIME, Constants.DEFAULT_COOKIETIME));
      response.addCookie(cookie);
      user.setLastLogin(new Date());
      userDAO.edit(user);
    }

    return user;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference<Cipher> cipherReference;
}
