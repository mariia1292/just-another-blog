/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.model;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.GenericDAO;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class GenericDataModel extends AbstractDataModel
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(GenericDataModel.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param dao
   * @param pageSize
   */
  public GenericDataModel(GenericDAO<?> dao, int pageSize)
  {
    super(pageSize);
    this.dao = dao;
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
    return dao;
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
    if (data instanceof GenericDAO<?>)
    {
      dao = (GenericDAO<?>) data;
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
    if (logger.isLoggable(Level.FINER))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("get row count from ").append(dao.getClass().getName());
      logger.finer(msg.toString());
    }

    return dao.count();
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
    if (logger.isLoggable(Level.FINER))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("fetching ").append(max).append(" items from ");
      msg.append(dao.getClass().getName());
      msg.append(", start at ").append(start);
      logger.finer(msg.toString());
    }

    return dao.getAll(start, max);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private GenericDAO<?> dao;
}
