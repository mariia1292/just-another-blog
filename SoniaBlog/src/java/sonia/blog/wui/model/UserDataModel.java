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


package sonia.blog.wui.model;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.entity.User;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class UserDataModel extends AbstractDataModel
{

  /**
   * Constructs ...
   *
   *
   * @param pageSize
   */
  public UserDataModel(int pageSize)
  {
    super(pageSize);
    this.active = false;
    this.filter = null;
    this.userDAO = BlogContext.getDAOFactory().getUserDAO();
  }

  /**
   * Constructs ...
   *
   *
   * @param pageSize
   * @param active
   */
  public UserDataModel(int pageSize, boolean active)
  {
    super(pageSize);
    this.active = active;
    this.filter = null;
    this.userDAO = BlogContext.getDAOFactory().getUserDAO();
  }

  /**
   * Constructs ...
   *
   *
   * @param pageSize
   * @param filter
   */
  public UserDataModel(int pageSize, String filter)
  {
    super(pageSize);
    this.active = false;
    this.filter = filter;
    this.userDAO = BlogContext.getDAOFactory().getUserDAO();
  }

  /**
   * Constructs ...
   *
   *
   * @param pageSize
   * @param filter
   * @param active
   */
  public UserDataModel(int pageSize, String filter, boolean active)
  {
    super(pageSize);
    this.active = active;
    this.filter = filter;
    this.userDAO = BlogContext.getDAOFactory().getUserDAO();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Object getWrappedData()
  {
    return userDAO;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param data
   */
  @Override
  public void setWrappedData(Object data)
  {
    if ((data != null) && (data instanceof UserDAO))
    {
      this.userDAO = (UserDAO) data;
    }
    else
    {
      throw new IllegalArgumentException();
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected long countData()
  {
    long result = -1;

    if (!Util.isBlank(filter) && active)
    {
      result = userDAO.count(filter, active);
    }
    else if (!Util.isBlank(filter))
    {
      result = userDAO.count(filter);
    }
    else if (active)
    {
      result = userDAO.count(active);
    }
    else
    {
      result = userDAO.count();
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  @Override
  protected List<?> getData(int start, int max)
  {
    List<User> result = null;

    if (!Util.isBlank(filter) && active)
    {
      result = userDAO.getAll(filter, active, start, max);
    }
    else if (!Util.isBlank(filter))
    {
      result = userDAO.getAll(filter, start, max);
    }
    else if (active)
    {
      result = userDAO.getAll(active, start, max);
    }
    else
    {
      result = userDAO.getAll(start, max);
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean active;

  /** Field description */
  private String filter;

  /** Field description */
  private UserDAO userDAO;
}