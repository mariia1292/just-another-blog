/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

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
    return null;
  }

  /**
   * Method description
   *
   */
  public abstract void close();

  /**
   * Method description
   *
   */
  public abstract void init();

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
  public abstract EntryDAO getEntryDAO();

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

  public abstract MemberDAO getMemberDAO();
}
