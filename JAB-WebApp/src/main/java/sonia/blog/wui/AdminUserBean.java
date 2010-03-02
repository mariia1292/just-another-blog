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
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.authentication.RequireRole;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;
import sonia.blog.wui.model.UserDataModel;

import sonia.config.Config;

import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

import sonia.security.encryption.Encryption;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author Sebastian Sdorra
 */
@RequireRole(Role.GLOBALADMIN)
public class AdminUserBean extends AbstractBean
{

  /** Field description */
  public static final String BACK = "back";

  /** Field description */
  public static final String DETAIL = "detail";

  /** Field description */
  public static final String NEW = "new";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AdminUserBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public AdminUserBean()
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
  public String addBlog()
  {
    BlogRequest request = getRequest();

    if (Util.hasContent(addBlogName))
    {
      Blog blog = blogDAO.get(addBlogName);

      if (blog != null)
      {
        userDAO.setRole(blog, user, Role.READER);
        getMessageHandler().info(request, "addBlogToUserSuccess");
        addBlogName = null;
      }
      else
      {
        getMessageHandler().warn(request, "addBlogToUserFailure");
      }
    }
    else
    {
      getMessageHandler().warn(request, "addBlogToUserFailure");
    }

    return DETAIL;
  }

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
   * @return
   */
  public String newUser()
  {
    user = new User();

    return NEW;
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

    if (member != null)
    {
      Blog blog = member.getBlog();
      User user = member.getUser();
      Role role = (Role) event.getNewValue();

      try
      {
        userDAO.setRole(blog, user, role);
        getMessageHandler().info(getRequest(), "changeRoleSuccess");
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error(getRequest(), "changeRoleFailure");
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
    String result = FAILURE;

    if (checkMail())
    {
      if (checkName())
      {
        BlogSession session = getBlogSession();

        if (user.getId() == null)
        {
          if (userDAO.add(session, user))
          {
            getMessageHandler().info(getRequest(), "userSettingsUpdateSuccess");
            result = SUCCESS;
          }
          else
          {
            getMessageHandler().error(getRequest(), "unknownError");
          }
        }
        else
        {
          if (userDAO.edit(session, user))
          {
            getMessageHandler().info(getRequest(), "userSettingsUpdateSuccess");
            result = SUCCESS;
          }
          else
          {
            getMessageHandler().error(getRequest(), "unknownError");
          }
        }
      }
      else
      {
        getMessageHandler().warn(getRequest(), null, "nameAllreadyExists",
                                 null, user.getName());
      }
    }
    else
    {
      getMessageHandler().warn(getRequest(), null, "emailAllreadyExists", null,
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
  public String saveWithPassword()
  {
    String result = SUCCESS;

    if (encReference != null)
    {
      Encryption enc = encReference.get();

      if (enc != null)
      {
        user.setPassword(enc.encrypt(passwordRetry));
        result = save();
      }
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
  public String getAddBlogName()
  {
    return addBlogName;
  }

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
    members = new ListDataModel();

    // TODO scrolling
    List<BlogMember> memberList = userDAO.getMembers(user, 0, 1000);

    if ((memberList != null) &&!memberList.isEmpty())
    {
      members.setWrappedData(memberList);
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
    SelectItem[] items = new SelectItem[4];
    ResourceBundle bundle = getResourceBundle("label");

    items[0] = new SelectItem(Role.READER,
                              bundle.getString(Role.READER.name()));
    items[1] = new SelectItem(Role.AUTHOR,
                              bundle.getString(Role.AUTHOR.name()));
    items[2] = new SelectItem(Role.CONTENTMANAGER,
                              bundle.getString(Role.CONTENTMANAGER.name()));
    items[3] = new SelectItem(Role.ADMIN, bundle.getString(Role.ADMIN.name()));

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
   * @param addBlogName
   */
  public void setAddBlogName(String addBlogName)
  {
    this.addBlogName = addBlogName;
  }

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
   *
   * @return
   */
  private boolean checkMail()
  {
    User u = userDAO.getByMail(user.getEmail());

    return (u == null) || u.equals(user);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private boolean checkName()
  {
    User u = userDAO.get(user.getName());

    return (u == null) || u.equals(user);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String addBlogName;

  /** Field description */
  @Dao
  private BlogDAO blogDAO;

  /** Field description */
  @Service(Constants.SERVCIE_ENCRYPTION)
  private ServiceReference<Encryption> encReference;

  /** Field description */
  private String filter;

  /** Field description */
  private DataModel members;

  /** Field description */
  private boolean onlyActive;

  /** Field description */
  @Config(Constants.CONFIG_ADMIN_PAGESIZE)
  private Integer pageSize = Integer.valueOf(20);

  /** Field description */
  private String passwordRetry;

  /** Field description */
  private User user;

  /** Field description */
  @Dao
  private UserDAO userDAO;

  /** Field description */
  private DataModel users;
}
