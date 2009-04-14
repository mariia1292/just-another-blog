/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.wui.model.CommentDataModel;

import sonia.config.Config;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;

/**
 *
 * @author sdorra
 */
public class CommentBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String details()
  {
    comment = (Comment) comments.getRowData();

    return "details";
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void remove(ActionEvent event)
  {
    Comment comm = (Comment) comments.getRowData();

    if (comm != null)
    {
      CommentDAO commentDAO = BlogContext.getDAOFactory().getCommentDAO();

      if (commentDAO.remove(comm))
      {
        getMessageHandler().info("removeCommentSuccess");
      }
      else
      {
        getMessageHandler().error("removeCommentFailure");
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void toggleSpam(ActionEvent event)
  {
    Comment comm = (Comment) comments.getRowData();
    CommentDAO commentDAO = BlogContext.getDAOFactory().getCommentDAO();

    comm.setSpam(!comm.isSpam());

    if (commentDAO.edit(comm))
    {
      getMessageHandler().info("toggleSpamSuccess");
    }
    else
    {
      getMessageHandler().error("toggleSpamFailure");
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Comment getComment()
  {
    return comment;
  }

  /**
   * Method description
   *
   * @return
   */
  public DataModel getComments()
  {
    Blog blog = getRequest().getCurrentBlog();

    comments = new CommentDataModel(blog, pageSize);

    return comments;
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

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param comment
   */
  public void setComment(Comment comment)
  {
    this.comment = comment;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Comment comment;

  /** Field description */
  private DataModel comments;

  /** Field description */
  @Config(Constants.CONFIG_ADMIN_PAGESIZE)
  private Integer pageSize = new Integer(20);
}
