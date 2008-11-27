/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
      EntityManager em = BlogContext.getInstance().getEntityManager();

      em.getTransaction().begin();

      try
      {
        Entry entry = comm.getEntry();

        em.remove(em.merge(comm));
        entry.getComments().remove(comment);
        em.merge(entry);
        em.getTransaction().commit();
        getMessageHandler().info("removeCommentSuccess");
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("removeCommentFailure");
      }
      finally
      {
        em.close();
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
    EntityManager em = BlogContext.getInstance().getEntityManager();

    em.getTransaction().begin();

    try
    {
      comm.setSpam(!comm.isSpam());
      em.merge(comm);
      em.getTransaction().commit();
      getMessageHandler().info("toggleSpamSuccess");
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      getMessageHandler().error("toggleSpamFailure");
    }
    finally
    {
      em.close();
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
   *
   * @return
   */
  public DataModel getComments()
  {
    comments = new ListDataModel();

    Blog blog = getRequest().getCurrentBlog();
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Comment.findFromBlog");

    q.setParameter("blog", blog);

    List list = q.getResultList();

    if (list != null)
    {
      comments.setWrappedData(list);
    }

    em.close();

    return comments;
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
}
