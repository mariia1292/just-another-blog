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

/**
 *
 * @author Sebastian Sdorra
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

    if (userDAO.edit(getBlogSession(), user))
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
