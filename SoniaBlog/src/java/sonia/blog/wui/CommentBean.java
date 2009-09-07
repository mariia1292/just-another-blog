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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.wui.model.CommentDataModel;

import sonia.config.Config;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Logger;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;

/**
 *
 * @author Sebastian Sdorra
 */
public class CommentBean extends AbstractBean
{

  /** Field description */
  private static Logger logger = Logger.getLogger(CommentBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public CommentBean()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

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

      if (commentDAO.remove(getBlogSession(), comm))
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

    if (commentDAO.edit(getBlogSession(), comm))
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
