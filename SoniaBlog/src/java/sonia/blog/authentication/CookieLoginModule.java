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

import sonia.blog.api.app.BlogConfiguration;
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

/**
 *
 * @author Sebastian Sdorra
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

            if (secretKey == null)
            {
              logger.severe("CookieKey is null");

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
    User user = userDAO.getByNameAndCode(username, activationCode);

    if ((user != null) && user.isActive())
    {
      cookie.setMaxAge(
          context.getConfiguration().getInteger(
            Constants.CONFIG_COKKIETIME, Constants.DEFAULT_COOKIETIME));
      user.setLastLogin(new Date());
      userDAO.edit(BlogContext.getInstance().getSystemBlogSession(), user);
    }

    return user;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private char[] getSecretKey()
  {
    char[] secretKey = null;
    BlogConfiguration config = BlogContext.getInstance().getConfiguration();
    String key = config.getString(Constants.CONFIG_COOKIEKEY);

    if (key != null)
    {
      secretKey = key.toCharArray();
    }

    return secretKey;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference<Cipher> cipherReference;
}
