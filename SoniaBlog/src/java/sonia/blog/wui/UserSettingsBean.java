/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.User;

import sonia.config.XmlConfiguration;

import sonia.plugin.service.ServiceReference;

import sonia.security.encryption.Encryption;

/**
 *
 * @author sdorra
 */
public class UserSettingsBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public UserSettingsBean()
  {
    super();

    XmlConfiguration config = BlogContext.getInstance().getConfiguration();

    passwordMinLength = config.getInteger(Constants.CONFIG_PASSWORD_MINLENGTH,
            Constants.DEFAULT_PASSWORD_MINLENGTH);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String updateDisplayName()
  {
    return save();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String updatePassword()
  {
    String result = null;

    if (passwordRetry.equals(user.getPassword()))
    {
      ServiceReference<Encryption> reference =
        BlogContext.getInstance().getServiceRegistry().get(Encryption.class,
          Constants.SERVCIE_ENCRYPTION);

      if (reference != null)
      {
        Encryption enc = reference.get();

        if (enc != null)
        {
          if (enc.encrypt(passwordOld).equals(
                  getRequest().getUser().getPassword()))
          {
            user.setPassword(enc.encrypt(passwordRetry));
            result = save();
          }
          else
          {
            getMessageHandler().warn("passwordIsWrong");
          }
        }
      }
    }
    else
    {
      getMessageHandler().warn("passwordsNotEqual");
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
  public int getPasswordMinLength()
  {
    return passwordMinLength;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPasswordOld()
  {
    return passwordOld;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPasswordRetry()
  {
    return passwordRetry;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public User getUser()
  {
    if (user == null)
    {
      Long id = getRequest().getUser().getId();

      user = BlogContext.getDAOFactory().getUserDAO().get(id);
    }

    return user;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param passwordOld
   */
  public void setPasswordOld(String passwordOld)
  {
    this.passwordOld = passwordOld;
  }

  /**
   * Method description
   *
   *
   * @param passwordRetry
   */
  public void setPasswordRetry(String passwordRetry)
  {
    this.passwordRetry = passwordRetry;
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
  private String save()
  {
    String result = SUCCESS;
    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

    if (userDAO.edit(user))
    {
      getMessageHandler().info("userSettingsUpdateSuccess");
    }
    else
    {
      result = FAILURE;
      getMessageHandler().error("unknownError");
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int passwordMinLength;

  /** Field description */
  private String passwordOld;

  /** Field description */
  private String passwordRetry;

  /** Field description */
  private User user;
}
