/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.cache.CacheManager;

import sonia.plugin.service.ServiceReference;

/**
 *
 * @author sdorra
 */
public abstract class DAOFactory
{

  /** Field description */
  private static DAOFactory instance;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static DAOFactory getInstance()
  {
    if (instance == null)
    {
      instance = createDAOFactory();
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private static DAOFactory createDAOFactory()
  {
    BlogContext context = BlogContext.getInstance();
    ServiceReference<DAOFactory> reference =
      context.getServiceRegistry().get(DAOFactory.class, Constants.SERVCIE_DAO);

    return reference.get();
  }

  /**
   * Method description
   *
   *
   * @throws DAOException
   */
  public abstract void close() throws DAOException;

  /**
   * Method description
   *
   *
   * @throws DAOException
   */
  public abstract void init() throws DAOException;

  /**
   * Method description
   *
   */
  public abstract void install();

  /**
   * Method description
   *
   */
  public abstract void update();

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract AttachmentDAO getAttachmentDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract BlogDAO getBlogDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract BlogHitCountDAO getBlogHitCountDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract CacheManager getCacheManager();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract CategoryDAO getCategoryDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract CommentDAO getCommentDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract Object getConnection();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract EntryDAO getEntryDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract PageDAO getPageDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract TagDAO getTagDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract UserDAO getUserDAO();
}
