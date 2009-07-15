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


package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.app.MailService;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Blog;
import sonia.blog.entity.User;

import sonia.plugin.service.Service;

import sonia.security.KeyGenerator;
import sonia.security.cipher.Cipher;
import sonia.security.encryption.Encryption;

import sonia.util.Convert;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

/**
 *
 * @author Sebastian Sdorra
 */
public class LostPasswordMapping extends FinalMapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(LostPasswordMapping.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    Blog blog = request.getCurrentBlog();
    String id = request.getParameter("id");

    if (Util.hasContent(id))
    {
      id = new String(Convert.fromBase64(id));
      id = cipher.decode(id);

      String[] parts = id.split(":");

      if ((parts != null) && (parts.length == 4)
          && parts[0].equals(blog.getId().toString()))
      {
        String username = parts[1];
        String activationCode = parts[2];
        String localeString = parts[3];
        Locale locale = new Locale(localeString);
        User user = userDAO.getByNameAndCode(username, activationCode);

        if ((user != null) && user.isActive() && user.isSelfManaged())
        {
          try
          {
            String password = KeyGenerator.generateKey(8);

            user.setPassword(encryption.encrypt(password));

            if (userDAO.edit(user))
            {
              sendMail(blog, locale, user, password);

              // TODO: add Message
            }
          }
          catch (Exception ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }
      }
    }

    response.sendRedirect(linkBuilder.buildLink(blog, ""));
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param locale
   * @param user
   * @param password
   */
  private void sendMail(Blog blog, Locale locale, User user, String password)
  {
    ResourceBundle bundle =
      ResourceBundle.getBundle("/sonia/blog/resources/message", locale,
                               LostPasswordMapping.class.getClassLoader());
    StringBuffer subject = new StringBuffer();

    subject.append("[").append(blog.getTitle()).append("] ");
    subject.append(bundle.getString("mailLostPasswordSubject"));

    String text = MessageFormat.format(bundle.getString("mailLostPassword"),
                                       password);

    try
    {
      mailService.sendMail(blog.getEmail(), user.getEmail(),
                           subject.toString(), text);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Service(Constants.SERVCIE_CIPHER)
  private Cipher cipher;

  /** Field description */
  @Service(Constants.SERVCIE_ENCRYPTION)
  private Encryption encryption;

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;

  /** Field description */
  @Context
  private MailService mailService;

  /** Field description */
  @Dao
  private UserDAO userDAO;
}