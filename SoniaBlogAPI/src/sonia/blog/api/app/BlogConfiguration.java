/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.config.XmlConfiguration;

import sonia.security.cipher.DefaultCipher;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Date;

/**
 *
 * @author sdorra
 */
public class BlogConfiguration extends XmlConfiguration
{

  /**
   * Constructs ...
   *
   */
  public BlogConfiguration()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void load() throws IOException
  {
    File file = BlogContext.getInstance().getConfigFile();

    if (file.exists())
    {
      FileInputStream fis = new FileInputStream(file);

      try
      {
        load(fis);

        String key = getString(Constants.CONFIG_SECUREKEY);

        if (Util.hasContent(key))
        {
          setCipher(new DefaultCipher(key.toCharArray()));
        }
      }
      finally
      {
        if (fis != null)
        {
          fis.close();
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void store() throws IOException
  {
    File file = BlogContext.getInstance().getConfigFile();
    File backupDir = new File(file.getParentFile(), "backup");

    if (!backupDir.exists())
    {
      backupDir.mkdirs();
    }

    File backupFile = new File(backupDir,
                               "config-" + new Date().getTime() + ".xml");

    file.renameTo(backupFile);

    FileOutputStream fos = new FileOutputStream(file);

    try
    {
      store(fos);
    }
    catch (IOException ex)
    {
      if (!file.exists() && backupFile.exists())
      {
        backupFile.renameTo(file);
      }

      throw ex;
    }
    finally
    {
      if (fos != null)
      {
        fos.close();
      }
    }
  }
}
