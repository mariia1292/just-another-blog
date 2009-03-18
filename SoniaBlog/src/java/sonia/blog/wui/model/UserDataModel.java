/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
