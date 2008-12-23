/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Entry;

import sonia.plugin.ServiceReference;

import sonia.rss.Channel;
import sonia.rss.FeedParser;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.net.URL;

import java.util.List;
import java.util.logging.Level;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

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
   *
   * @return
   */
  public long getAttachmentCount()
  {
    return countQuery("Attachment.countFromBlog");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getCategoryCount()
  {
    return countQuery("Category.countFromBlog");
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
   *
   * @return
   */
  public long getCommentCount()
  {
    return countQuery("Comment.countFromBlog");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getComments()
  {
    if (comments == null)
    {
      EntityManager em = BlogContext.getInstance().getEntityManager();

      try
      {
        Query q = em.createNamedQuery("Comment.findFromBlog");

        q.setParameter("blog", getRequest().getCurrentBlog());
        q.setMaxResults(5);
        comments = new ListDataModel(q.getResultList());
      }
      catch (NoResultException ex)
      {

        // do nothing
      }
      finally
      {
        em.close();
      }
    }

    return comments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public DataModel getDrafts()
  {
    if (drafts == null)
    {
      BlogRequest request = getRequest();
      EntityManager em = BlogContext.getInstance().getEntityManager();
      Query q = em.createNamedQuery("Entry.findDraftsOfUser");

      q.setParameter("user", request.getUser());
      q.setParameter("blog", request.getCurrentBlog());
      q.setMaxResults(10);

      List draftList = q.getResultList();

      drafts = new ListDataModel(draftList);
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
   *
   * @return
   */
  public long getEntryCount()
  {
    return countQuery("Entry.countFromBlog");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getMemberCount()
  {
    return countQuery("BlogMember.countFromBlog");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTagCount()
  {
    return 0;//countQuery("Tag.countFromBlog");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getWidgets()
  {
    if (widgets == null)
    {
      ServiceReference dashboardWidgetReference =
        BlogContext.getInstance().getServiceRegistry().getServiceReference(
            Constants.SERVICE_DASHBOARDWIDGET);

      widgets = dashboardWidgetReference.getImplementations();
    }

    return widgets;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param queryName
   *
   * @return
   */
  private long countQuery(String queryName)
  {
    long result = 0;
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery(queryName);

    q.setParameter("blog", getRequest().getCurrentBlog());
    result = (Long) q.getSingleResult();
    em.close();

    return result;
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
