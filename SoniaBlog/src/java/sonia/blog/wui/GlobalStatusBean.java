/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.util.AbstractBean;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.List;

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
      BlogContext.getInstance().getServiceRegistry().get(String.class,
        Constants.SERVCIE_GLOBALSTATUSROVIDER);
    resourceDir =
      BlogContext.getInstance().getResourceManager().getResourceDirectory();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   * @return
   */
  public long getAttachmentCount()
  {
    return BlogContext.getDAOFactory().getAttachmentDAO().count();
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

    if (attachments.exists())
    {
      for (File dir : attachments.listFiles())
      {
        size += Util.getLength(new File(dir, Constants.RESOURCE_ENTRIES));
      }
    }

    return Util.formatSize((double) size);
  }

  /**
   * Method description
   *
   * @return
   */
  public long getBlogCount()
  {
    return BlogContext.getDAOFactory().getBlogDAO().count();
  }

  /**
   * Method description
   *
   * @return
   */
  public long getCategoryCount()
  {
    return BlogContext.getDAOFactory().getCategoryDAO().count();
  }

  /**
   * Method description
   *
   * @return
   */
  public long getCommentCount()
  {
    return BlogContext.getDAOFactory().getCommentDAO().count();
  }

  /**
   * Method description
   *
   * @return
   */
  public long getEntryCount()
  {
    return BlogContext.getDAOFactory().getEntryDAO().count();
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

    if (attachments.exists())
    {
      for (File dir : attachments.listFiles())
      {
        size += Util.getLength(new File(dir, Constants.RESOURCE_IMAGE));
      }
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
  public List<String> getStatusProviders()
  {
    return reference.getAll();
  }

  /**
   * Method description
   *
   * @return
   */
  public long getTagCount()
  {
    return BlogContext.getDAOFactory().getTagDAO().count();
  }

  /**
   * Method description
   *
   * @return
   */
  public long getUserCount()
  {
    return BlogContext.getDAOFactory().getUserDAO().count();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference<String> reference;

  /** Field description */
  private File resourceDir;
}
