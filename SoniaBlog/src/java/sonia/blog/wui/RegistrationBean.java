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
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.config.Config;

import sonia.plugin.service.Service;

import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class RegistrationBean extends AbstractBean
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(RegistrationBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public RegistrationBean()
  {
    init();
    this.user = new User();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;

    if (registrationEnabled)
    {
      user.setPassword(encryptPassword(passwordRepeat));
      result = createUser();

      if (sendAcknowledgeMail)
      {
        sendMail();
      }

      redirect();
    }
    else
    {
      getMessageHandler().error("registrationDisabled");
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
  public String getPasswordRepeat()
  {
    return passwordRepeat;
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

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param passwordRepeat
   */
  public void setPasswordRepeat(String passwordRepeat)
  {
    this.passwordRepeat = passwordRepeat;
  }

  /**
   * Method description
   *
   *
   * @param user
   */
  public void setUser(User user)
  {
    this.user = user;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private String createUser()
  {
    String result = SUCCESS;

    if (userDAO.add(user))
    {
      Blog blog = getRequest().getCurrentBlog();
      Role role = Role.valueOf(defaultRole);

      try
      {
        userDAO.setRole(blog, user, role);
        getMessageHandler().info("registrationSuccess");
      }
      catch ( /* TODO replace with DAOException */Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("unknownError");
      }
    }
    else
    {
      getMessageHandler().error("unknownError");
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param password
   *
   * @return
   */
  private String encryptPassword(String password)
  {
    if (enc != null)
    {
      password = enc.encrypt(password);
    }

    return password;
  }

  /**
   * Method description
   *
   */
  private void redirect()
  {
    BlogRequest request = getRequest();
    String uri = linkBuilder.buildLink(request, request.getCurrentBlog());

    sendRedirect(uri);
  }

  /**
   * Method description
   *
   */
  private void sendMail()
  {
    BlogRequest request = getRequest();

    try
    {
      mailService.sendMail(request.getCurrentBlog().getEmail(),
                           user.getEmail(), "subject", "text");
    }
    catch (Exception ex)
    {
      getMessageHandler().error("unknownError");
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Config(Constants.CONFIG_DEFAULTROLE)
  private String defaultRole = Role.READER.name();

  /** Field description */
  @Service(Constants.SERVCIE_ENCRYPTION)
  private Encryption enc;

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;

  /** Field description */
  @Context
  private MailService mailService;

  /** Field description */
  private String passwordRepeat;

  /** Field description */
  @Config(Constants.CONFIG_ALLOW_REGISTRATION)
  private Boolean registrationEnabled = Boolean.FALSE;

  /** Field description */
  @Config(Constants.CONFIG_REGISTERACKNOWLEDGEMENT)
  private Boolean sendAcknowledgeMail = Boolean.FALSE;

  /** Field description */
  private User user;

  /** Field description */
  @Dao
  private UserDAO userDAO;
}
