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
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.User;

import sonia.config.Config;

import sonia.plugin.service.ServiceReference;

import sonia.rss.Channel;
import sonia.rss.FeedParser;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.net.URL;
import java.net.UnknownHostException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Sebastian Sdorra
 */
public class DashboardBean extends AbstractBean
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DashboardBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DashboardBean()
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
  public String editDraft()
  {
    String result = "edit-draft";
    Entry draft = (Entry) drafts.getRowData();

    if (draft != null)
    {
      EntryBean entryBean = getEntryBean();

      entryBean.edit(draft);
    }
    else
    {
      result = FAILURE;
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   * @return
   */
  public long getAttachmentCount()
  {
    Blog b = getRequest().getCurrentBlog();
    AttachmentDAO attachmentDAO =
      BlogContext.getDAOFactory().getAttachmentDAO();

    return attachmentDAO.countByBlog(b);
  }

  /**
   * Method description
   *
   * @return
   */
  public long getCategoryCount()
  {
    Blog b = getRequest().getCurrentBlog();
    CategoryDAO categegoryDAO = BlogContext.getDAOFactory().getCategoryDAO();

    return categegoryDAO.count(b);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Channel getChannel()
  {
    if (channel == null)
    {
      try
      {
        FeedParser parser = FeedParser.getInstance("rss2");

        channel = parser.load(new URL(rssUrl).openStream());
      }
      catch (UnknownHostException ex)
      {
        logger.log(Level.FINEST, null, ex);
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return channel;
  }

  /**
   * Method description
   *
   * @return
   */
  public long getCommentCount()
  {
    Blog b = getRequest().getCurrentBlog();
    CommentDAO commentDAO = BlogContext.getDAOFactory().getCommentDAO();

    return commentDAO.countByBlog(b);
  }

  /**
   * Method description
   * TODO show only 5
   *
   * @return
   */
  public DataModel getComments()
  {
    if (comments == null)
    {
      comments = new ListDataModel();

      CommentDAO commentDAO = BlogContext.getDAOFactory().getCommentDAO();
      List<Comment> commentList =
        commentDAO.findAllByBlog(getRequest().getCurrentBlog(), 0, 5);

      if ((commentList != null) &&!commentList.isEmpty())
      {
        comments.setWrappedData(commentList);
      }
    }

    return comments;
  }

  /**
   * Method description
   *
   * @return
   */
  public DataModel getDrafts()
  {
    if (drafts == null)
    {
      drafts = new ListDataModel();

      EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
      BlogRequest request = getRequest();
      Blog blog = request.getCurrentBlog();
      User user = request.getUser();
      List<Entry> draftList = entryDAO.findAllDraftsByBlogAndUser(blog, user,
                                0, 5);

      if ((draftList != null) &&!draftList.isEmpty())
      {
        drafts.setWrappedData(draftList);
      }
    }

    return drafts;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public EntryBean getEntryBean()
  {
    EntryBean bean = null;
    FacesContext context = FacesContext.getCurrentInstance();

    bean =
      (EntryBean) context.getExternalContext().getSessionMap().get("EntryBean");

    if (bean == null)
    {
      bean = new EntryBean();
      context.getExternalContext().getSessionMap().put("EntryBean", bean);
    }

    return bean;
  }

  /**
   * Method description
   *
   * @return
   */
  public long getEntryCount()
  {
    Blog b = getRequest().getCurrentBlog();
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();

    return entryDAO.countByBlog(b);
  }

  /**
   * Method description
   *
   * @return
   */
  public long getMemberCount()
  {
    Blog b = getRequest().getCurrentBlog();
    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

    return userDAO.count(b);
  }

  /**
   * Method description
   * TODO: replace with TagDAO.countByBlog()
   *
   * @return
   */
  public long getTagCount()
  {
    return 0;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<String> getWidgets()
  {
    if (widgets == null)
    {
      ServiceReference<String> dashboardWidgetReference =
        BlogContext.getInstance().getServiceRegistry().get(String.class,
          Constants.SERVICE_DASHBOARDWIDGET);

      widgets = dashboardWidgetReference.getAll();
    }

    return widgets;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Channel channel;

  /** Field description */
  private DataModel comments;

  /** Field description */
  private DataModel drafts;

  /** Field description */
  @Config(Constants.CONFIG_DASHBOARD_RSS)
  private String rssUrl = Constants.DEFAULT_DASHBOARD_RSS;

  /** Field description */
  private List<String> widgets;
}