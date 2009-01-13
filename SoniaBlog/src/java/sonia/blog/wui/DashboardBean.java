/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import sonia.blog.api.dao.MemberDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.User;

import sonia.plugin.service.ServiceReference;

import sonia.rss.Channel;
import sonia.rss.FeedParser;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.net.URL;
import java.net.UnknownHostException;

import java.util.List;
import java.util.logging.Level;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author sdorra
 */
public class DashboardBean extends AbstractBean
{

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

    return categegoryDAO.countByBlog(b);
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

        channel = parser.load(
          new URL("http://rss.golem.de/rss.php?feed=RSS2.0").openStream());
      }
      catch (UnknownHostException ex)
      {
        logger.log(Level.FINE, null, ex);
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
      List<Entry> draftList = entryDAO.findAllDraftsByBlogAndUser(blog, user, 0, 5);

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
    MemberDAO memberDAO = BlogContext.getDAOFactory().getMemberDAO();

    return memberDAO.countByBlog(b);
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
  private List<String> widgets;
}
