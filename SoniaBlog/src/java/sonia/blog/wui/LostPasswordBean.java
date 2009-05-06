/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
