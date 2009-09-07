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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.app.MailService;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.User;

import sonia.plugin.service.Service;

import sonia.security.cipher.Cipher;

import sonia.util.Convert;

//~--- JDK imports ------------------------------------------------------------

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class LostPasswordBean extends AbstractBean
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(LostPasswordBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public LostPasswordBean()
  {
    init();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String send()
  {
    String result = SUCCESS;
    User u = userDAO.get(username, true);

    if ((u != null) && u.getEmail().equals(email))
    {
      BlogRequest request = getRequest();
      Blog blog = request.getCurrentBlog();
      ResourceBundle bundle = getResourceBundle("message");
      StringBuffer subject = new StringBuffer();

      subject.append("[").append(blog.getTitle()).append("] ");
      subject.append(bundle.getString("mailLostPasswordConfirmationSubject"));

      Locale l = getLocale();
      StringBuffer idBuffer = new StringBuffer();

      idBuffer.append(blog.getId()).append(":").append(username).append(":");
      idBuffer.append(u.getActivationCode()).append(":");
      idBuffer.append(l.getLanguage());

      String id = cipher.encode(idBuffer.toString());
      String link = linkBuilder.buildLink(request, "/lost-password?id=");
      String s = System.getProperty("line.separator");

      try
      {
        StringBuffer text = new StringBuffer();

        text.append(bundle.getString("mailLostPasswordConfirmation"));
        text.append(s).append(s).append(link).append(
            Convert.toBase64(id.getBytes()));
        mailService.sendMail(u.getEmail(), blog.getEmail(), subject.toString(),
                             text.toString());

        // TODO: add Message
        getResponse().sendRedirect(linkBuilder.buildLink(blog, ""));
      }
      catch (Exception ex)
      {
        result = FAILURE;
        logger.log(Level.SEVERE, null, ex);
      }
    }
    else
    {
      result = FAILURE;
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getEmail()
  {
    return email;
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

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param email
   */
  public void setEmail(String email)
  {
    this.email = email;
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Service(Constants.SERVCIE_CIPHER)
  private Cipher cipher;

  /** Field description */
  private String email;

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;

  /** Field description */
  @Context
  private MailService mailService;

  /** Field description */
  @Dao
  private UserDAO userDAO;

  /** Field description */
  private String username;
}
