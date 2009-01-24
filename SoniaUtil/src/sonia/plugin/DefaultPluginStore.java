/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class DefaultPluginStore implements PluginStore
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultPluginStore.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DefaultPluginStore()
  {
    storeDir = new File(System.getProperty("java.io.tmpdir"), "plugin.store");

    if (!storeDir.exists() &&!storeDir.mkdirs())
    {
      throw new RuntimeException("could not create store directory");
    }
  }

  /**
   * Constructs ...
   *
   *
   * @param storeDir
   */
  public DefaultPluginStore(File storeDir)
  {
    this.storeDir = storeDir;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void startPlugin(Plugin plugin)
  {
    File file = new File(storeDir, plugin.getName());

    if (file.exists() &&!file.delete())
    {
      throw new RuntimeException("could not delete file " + file.getPath());
    }
  }

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void stopPlugin(Plugin plugin)
  {
    File file = new File(storeDir, plugin.getName());

    if (!file.exists())
    {
      try
      {
        file.createNewFile();
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param plugin
   *
   * @return
   */
  public boolean isStartAble(Plugin plugin)
  {
    File file = new File(storeDir, plugin.getName());

    return !file.exists();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File storeDir;
}
