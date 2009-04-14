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
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;
import sonia.blog.wui.model.UserDataModel;

import sonia.config.Config;

import sonia.plugin.service.ServiceReference;

import sonia.security.encryption.Encryption;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author sdorra
 */
public class AdminUserBean extends AbstractBean
{

  /** Field description */
  public static final String BACK = "back";

  /** Field description */
  public static final String DETAIL = "detail";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String detail()
  {
    user = (User) users.getRowData();

    return DETAIL;
  }

  /**
   * Method description
   *
   *
   *
   * @param event
   */
  public void roleChanged(ValueChangeEvent event)
  {
    BlogMember member = (BlogMember) members.getRowData();
    Blog blog = member.getBlog();
    User user = member.getUser();
    Role role = (Role) event.getNewValue();

    if (member != null)
    {
      try
      {
        UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

        userDAO.setRole(blog, user, role);
        getMessageHandler().info("changeRoleSuccess");
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("changeRoleFailure");
      }
    }
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
    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

    if (checkMail(userDAO))
    {
      if (userDAO.edit(user))
      {
        getMessageHandler().info("userSettingsUpdateSuccess");
      }
      else
      {
        result = FAILURE;
        getMessageHandler().error("unknownError");
      }
    }
    else
    {
      getMessageHandler().warn(null, "emailAllreadyExists", null,
                               user.getEmail());
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String savePassword()
  {
    String result = SUCCESS;

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
          user.setPassword(enc.encrypt(passwordRetry));
          result = save();
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
  public String getFilter()
  {
    return filter;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getMembers()
  {
    if (members == null)
    {
      members = new ListDataModel();

      UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

      // TODO scrolling
      List<BlogMember> memberList = userDAO.getMembers(user, 0, 1000);

      if ((memberList != null) &&!memberList.isEmpty())
      {
        members.setWrappedData(memberList);
      }
    }

    return members;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Integer getPageSize()
  {
    return pageSize;
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
  public SelectItem[] getRoleItems()
  {
    SelectItem[] items = new SelectItem[3];
    ResourceBundle bundle = getResourceBundle("label");

    items[0] = new SelectItem(Role.READER,
                              bundle.getString(Role.READER.name()));
    items[1] = new SelectItem(Role.AUTHOR,
                              bundle.getString(Role.AUTHOR.name()));
    items[2] = new SelectItem(Role.ADMIN, bundle.getString(Role.ADMIN.name()));

    return items;
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

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getUsers()
  {
    if (!Util.isBlank(filter) && onlyActive)
    {
      users = new UserDataModel(pageSize, filter, onlyActive);
    }
    else if (!Util.isBlank(filter))
    {
      users = new UserDataModel(pageSize, filter);
    }
    else if (onlyActive)
    {
      users = new UserDataModel(pageSize, onlyActive);
    }
    else
    {
      users = new UserDataModel(pageSize);
    }

    return users;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isOnlyActive()
  {
    return onlyActive;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param filter
   */
  public void setFilter(String filter)
  {
    this.filter = filter;
  }

  /**
   * Method description
   *
   *
   * @param members
   */
  public void setMembers(DataModel members)
  {
    this.members = members;
  }

  /**
   * Method description
   *
   *
   * @param onlyActive
   */
  public void setOnlyActive(boolean onlyActive)
  {
    this.onlyActive = onlyActive;
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

  /**
   * Method description
   *
   *
   * @param users
   */
  public void setUsers(DataModel users)
  {
    this.users = users;
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
  private boolean checkMail(UserDAO userDAO)
  {
    User u = userDAO.getByMail(user.getEmail());

    return (u == null) || u.equals(user);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String filter;

  /** Field description */
  private DataModel members;

  /** Field description */
  private boolean onlyActive;

  /** Field description */
  @Config(Constants.CONFIG_ADMIN_PAGESIZE)
  private Integer pageSize = new Integer(20);

  /** Field description */
  private String passwordRetry;

  /** Field description */
  private User user;

  /** Field description */
  private DataModel users;
}
