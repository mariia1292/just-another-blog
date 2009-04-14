/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.model;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public class CommentDataModel extends AbstractDataModel
{

  /**
   * Constructs ...
   *
   *
   * @param blog
   * @param pageSize
   */
  public CommentDataModel(Blog blog, int pageSize)
  {
    super(pageSize);
    this.blog = blog;
    this.commentDAO = BlogContext.getDAOFactory().getCommentDAO();
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
    return commentDAO;
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
    if (data instanceof CommentDAO)
    {
      this.commentDAO = (CommentDAO) data;
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
    return commentDAO.countByBlog(blog);
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
    return commentDAO.findAllByBlog(blog, start, max);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private CommentDAO commentDAO;
}
