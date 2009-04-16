/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;
import sonia.blog.util.BlogUtil;

import sonia.config.Configuration;

import sonia.plugin.service.ServiceReference;

import sonia.security.encryption.Encryption;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;

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
    super();
    this.user = new User();
    sendAcknowledgeMail =
      BlogContext.getInstance().getConfiguration().getBoolean(
        Constants.CONFIG_REGISTERACKNOWLEDGEMENT, false);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param userDAO
   *
   * @return
   */
  public String checkNameAndEmail(UserDAO userDAO)
  {
    String result = SUCCESS;
    User u = null;

    u = userDAO.get(user.getName());

    if (u != null)
    {
      getMessageHandler().error("regform:username", "nameAllreadyExists", null,
                                user.getName());
      result = FAILURE;
    }
    else
    {
      u = userDAO.getByMail(user.getEmail());

      if (u != null)
      {
        getMessageHandler().error("regform:email", "emailAllreadyExists", null,
                                  user.getEmail());
        result = FAILURE;
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;

    if (isPermitted())
    {
      if (user.getPassword().equals(passwordRepeat))
      {
        user.setPassword(encryptPassword(passwordRepeat));

        UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

        result = checkNameAndEmail(userDAO);

        if (result.equals(SUCCESS))
        {
          result = createUser(userDAO);

          if (sendAcknowledgeMail)
          {
            sendMail();
          }
        }

        redirect();
      }
      else
      {
        getMessageHandler().error("passwordsNotEqual");
        result = FAILURE;
      }
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
   * @param userDAO
   *
   * @return
   */
  private String createUser(UserDAO userDAO)
  {
    String result = SUCCESS;

    if (userDAO.add(user))
    {
      Blog blog = getRequest().getCurrentBlog();
      Role role = getDefaultRole();

      try
      {
        userDAO.setRole(blog, user, role);
        getMessageHandler().info("registrationSuccess");
      }
      catch ( /* TDOD replace with DAOException */Exception ex)
      {
        getMessageHandler().error("unknownError");
      }
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
    ServiceReference<Encryption> reference =
      BlogContext.getInstance().getServiceRegistry().get(Encryption.class,
        Constants.SERVCIE_ENCRYPTION);

    if ((reference != null) && (reference.get() != null))
    {
      Encryption enc = reference.get();

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
    String uri = BlogContext.getInstance().getLinkBuilder().buildLink(request,
                   request.getCurrentBlog());

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
      BlogUtil.sendMail(request.getCurrentBlog().getEmail(), user.getEmail(),
                        "subject", "text");
    }
    catch (MessagingException ex)
    {
      getMessageHandler().error("unknownError");
      logger.log(Level.SEVERE, null, ex);
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
   */
  private boolean isPermitted()
  {
    Configuration config = BlogContext.getInstance().getConfiguration();

    return config.getBoolean(Constants.CONFIG_ALLOW_REGISTRATION,
                             Boolean.FALSE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String passwordRepeat;

  /** Field description */
  private boolean sendAcknowledgeMail;

  /** Field description */
  private User user;
}
