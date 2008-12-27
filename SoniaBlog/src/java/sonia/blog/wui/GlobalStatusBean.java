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
   *
   *
   * @return
   */
  public long getAttachmentCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Attachment.countAll");
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
    long size = Util.getLength(new File(resourceDir, "attachment"));

    return Util.formatSize((double) size);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getBlogCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Blog.countAll");
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
  public long getCategoryCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Category.countAll");
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
  public long getCommentCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Comment.countAll");
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
  public long getEntryCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Entry.countAll");
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
    long size = Util.getLength(new File(resourceDir, "image"));

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
    long size = Util.getLength(new File(resourceDir, "index"));

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
   *
   *
   * @return
   */
  public long getTagCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Tag.countAll");
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
  public long getUserCount()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("User.countAll");
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
