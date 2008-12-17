/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

import sonia.config.XmlConfiguration;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;

import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author sdorra
 */
public abstract class AbstractConfigBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public AbstractConfigBean()
  {
    super();
    this.config = BlogContext.getInstance().getConfiguration();
    load(config);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param config
   */
  public abstract void load(XmlConfiguration config);

  /**
   * Method description
   *
   *
   * @param config
   */
  public abstract void store(XmlConfiguration config);

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    store(config);

    String result = SUCCESS;
    File file = BlogContext.getInstance().getConfigFile();
    File backupDir = new File(file.getParentFile(), "backup");

    if (!backupDir.exists())
    {
      backupDir.mkdirs();
    }

    File backupFile = new File(backupDir,
                               "config-" + new Date().getTime() + ".xml");

    try
    {
      file.renameTo(backupFile);
      config.store(new FileOutputStream(file));
      getMessageHandler().info("saveConfigSuccess");
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      getMessageHandler().error("unknownError");
      result = FAILURE;

      if (!file.exists() && backupFile.exists())
      {
        backupFile.renameTo(file);
      }
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private XmlConfiguration config;
}
