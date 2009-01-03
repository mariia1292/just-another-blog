/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.authentication;

//~--- non-JDK imports --------------------------------------------------------

import java.util.Date;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.authentication.SSOLoginModule;
import sonia.blog.entity.User;

import sonia.plugin.ServiceReference;

import sonia.security.cipher.Cipher;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import javax.security.auth.login.LoginException;

import javax.servlet.http.Cookie;

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
        BlogContext.getInstance().getServiceRegistry().getServiceReference(
          Constants.SERVCIE_CIPHER);
    }

    cipher = (Cipher) cipherReference.getImplementation();

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
            value = cipher.decode(Constants.SECRET_KEY, value);
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
    User user = null;
    BlogContext context = BlogContext.getInstance();
    EntityManager em = context.getEntityManager();

    try
    {
      // TODO: replace with UserDAO.findByNameAndCode()
      Query q = em.createNamedQuery("User.findByNameAndCode");

      q.setParameter("name", username);
      q.setParameter("code", activationCode);
      user = (User) q.getSingleResult();

      if (user != null)
      {
        cookie.setMaxAge(
            context.getConfiguration().getInteger(
              Constants.CONFIG_COKKIETIME, Constants.DEFAULT_COOKIETIME));
        response.addCookie(cookie);
        em.getTransaction().begin();
        user.setLastLogin(new Date());
        user = em.merge(user);
        em.getTransaction().commit();
      }
    }
    catch (NoResultException ex) {}
    catch (Exception ex)
    {
      if ( em.getTransaction().isActive() )
      {
        em.getTransaction().rollback();
      }
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      em.close();
    }

    return user;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference cipherReference;
}
