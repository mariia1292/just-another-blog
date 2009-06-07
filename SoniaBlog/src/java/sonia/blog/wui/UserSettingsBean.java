/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.User;

import sonia.plugin.service.ServiceReference;

import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class UserSettingsBean extends AbstractBean
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(UserSettingsBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public UserSettingsBean()
  {
    super();

    BlogConfiguration config = BlogContext.getInstance().getConfiguration();

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
  public String saveMember()
  {
    String result = SUCCESS;

    if (userDAO.saveMember(member))
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
  public BlogMember getMember()
  {
    if (member == null)
    {
      Blog b = getRequest().getCurrentBlog();
      User u = getUser();

      member = userDAO.getMember(b, u);
    }

    return member;
  }

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
   * @param member
   */
  public void setMember(BlogMember member)
  {
    this.member = member;
  }

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
  private BlogMember member;

  /** Field description */
  private int passwordMinLength;

  /** Field description */
  private String passwordOld;

  /** Field description */
  private String passwordRetry;

  /** Field description */
  private User user;

  /** Field description */
  @Dao
  private UserDAO userDAO;
}
