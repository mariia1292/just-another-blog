/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.util.AbstractBean;

import sonia.plugin.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class GlobalStatusBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public GlobalStatusBean()
  {
    super();
    reference =
      BlogContext.getInstance().getServiceRegistry().getServiceReference(
        Constants.SERVCIE_GLOBALSTATUSROVIDER);
    resourceDir =
      BlogContext.getInstance().getResourceManager().getResourceDirectory();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   * TODO: replace with AttachmentDAO.count
   *
   * @return
   */
  public long getAttachmentCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Attachment.count");
    Long result = (Long) q.getSingleResult();

    em.close();

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAttachmentSize()
  {
    long size = 0;
    File attachments = new File(resourceDir, Constants.RESOURCE_ATTACHMENT);

    for (File dir : attachments.listFiles())
    {
      size += Util.getLength(new File(dir, Constants.RESOURCE_ENTRIES));
    }

    return Util.formatSize((double) size);
  }

  /**
   * Method description
   * TODO: replace with BlogDAO.count
   *
   * @return
   */
  public long getBlogCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Blog.count");
    Long result = (Long) q.getSingleResult();

    em.close();

    return result;
  }

  /**
   * Method description
   * TODO: replace with CategoryDAO.count
   *
   * @return
   */
  public long getCategoryCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Category.count");
    Long result = (Long) q.getSingleResult();

    em.close();

    return result;
  }

  /**
   * Method description
   * TODO: replace with CommentDAO.count
   *
   * @return
   */
  public long getCommentCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Comment.count");
    Long result = (Long) q.getSingleResult();

    em.close();

    return result;
  }

  /**
   * Method description
   * TODO: replace with EntryDAO.count
   *
   * @return
   */
  public long getEntryCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Entry.count");
    Long result = (Long) q.getSingleResult();

    em.close();

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getImageSize()
  {
    long size = 0;
    File attachments = new File(resourceDir, Constants.RESOURCE_ATTACHMENT);

    for (File dir : attachments.listFiles())
    {
      size += Util.getLength(new File(dir, Constants.RESOURCE_IMAGE));
    }

    return Util.formatSize((double) size);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getIndexSize()
  {
    long size = Util.getLength(new File(resourceDir, Constants.RESOURCE_INDEX));

    return Util.formatSize((double) size);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getResourceDirectorySize()
  {
    long size = Util.getLength(resourceDir);

    return Util.formatSize((double) size);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getStatusProviders()
  {
    return reference.getImplementations();
  }

  /**
   * Method description
   * TODO: replace with TagDAO.count
   *
   * @return
   */
  public long getTagCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Tag.count");
    Long result = (Long) q.getSingleResult();

    em.close();

    return result;
  }

  /**
   * Method description
   * TODO: replace with UserDAO.count
   *
   * @return
   */
  public long getUserCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("User.count");
    Long result = (Long) q.getSingleResult();

    em.close();

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference reference;

  /** Field description */
  private File resourceDir;
}
