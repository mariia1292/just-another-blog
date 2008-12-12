/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.api.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import sonia.blog.api.app.BlogContext;
import sonia.config.XmlConfiguration;

/**
 *
 * @author sdorra
 */
public abstract class AbstractConfigBean extends AbstractBean {

  private XmlConfiguration config;

  /** Field description */
  private static SimpleDateFormat sdf =
    new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

  public abstract void load( XmlConfiguration config );

  public abstract void store( XmlConfiguration config );

  public AbstractConfigBean()
  {
    super();
    this.config = BlogContext.getInstance().getConfiguration();
    load(config);
  }


  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    store( config );

    String result = SUCCESS;
    File file = BlogContext.getInstance().getConfigFile();
    File backupDir = new File(file.getParentFile(), "backup");

    if (!backupDir.exists())
    {
      backupDir.mkdirs();
    }

    File backupFile = new File(backupDir,
                               "config-" + sdf.format(new Date()) + ".xml");

    try
    {
      file.renameTo(backupFile);
      config.store(new FileOutputStream(file));
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


}
